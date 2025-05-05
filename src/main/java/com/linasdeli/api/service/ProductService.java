package com.linasdeli.api.service;

import com.linasdeli.api.domain.*;
import com.linasdeli.api.domain.enums.PriceType;
import com.linasdeli.api.dto.CategoryCountDTO;
import com.linasdeli.api.dto.ProductDTO;
import com.linasdeli.api.dto.request.ProductRequestDTO;
import com.linasdeli.api.dto.response.CustomerProductDTO;
import com.linasdeli.api.dto.response.CustomerProductListDTO;
import com.linasdeli.api.dto.response.ProductFormResponseDTO;
import com.linasdeli.api.dto.response.ProductResponseDTO;
import com.linasdeli.api.repository.*;
import com.linasdeli.api.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final AnimalRepository animalRepository;
    private final CountryRepository countryRepository;
    private final CostRepository costRepository;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;


    public ProductService(ProductRepository productRepository,
                          SupplierRepository supplierRepository,
                          CategoryRepository categoryRepository,
                          AnimalRepository animalRepository,
                          CountryRepository countryRepository,
                          CostRepository costRepository,
                          ModelMapper modelMapper, FileUtil fileUtil) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.categoryRepository = categoryRepository;
        this.animalRepository = animalRepository;
        this.countryRepository = countryRepository;
        this.costRepository = costRepository;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
    }

    public ProductDTO createProduct(ProductRequestDTO dto, MultipartFile productImage, MultipartFile ingredientsImage) {

        // 엔티티들 조회
        Supplier supplier = supplierRepository.findById(dto.getSupplierId()).orElseThrow();
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();
        Animal animal = animalRepository.findById(dto.getAnimalId()).orElseThrow();
        Country country = countryRepository.findById(dto.getOriginId()).orElseThrow();

        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setSupplier(supplier);
        product.setCategory(category);
        product.setAllergies(dto.getAllergies());
        product.setPasteurized(dto.getPasteurized());
        product.setDescription(dto.getDescription());
        product.setServingSuggestion(dto.getSuggestion());
        product.setInStock(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // ✅ 이미지 저장 (파일명 + URL)
        if (productImage != null && !productImage.isEmpty()) {
            FileUtil.UploadResult result = fileUtil.saveImage(productImage, "product");
            product.setImageUrl(result.getUrl());
            product.setImageName(result.getFileName());
        }

        if (ingredientsImage != null && !ingredientsImage.isEmpty()) {
            FileUtil.UploadResult result = fileUtil.saveImage(ingredientsImage, "ingredients");
            product.setIngredientsImageUrl(result.getUrl());
            product.setIngredientsImageName(result.getFileName());
        }

        // ProductDetail
        ProductDetail detail = new ProductDetail();
        detail.setProduct(product);
        detail.setAnimal(animal);
        detail.setCountry(country);
        product.setProductDetails(List.of(detail));

        // 저장
        Product savedProduct = productRepository.save(product);

        Cost cost = new Cost();
        cost.setProduct(savedProduct);
        cost.setPriceType(dto.getPriceType());
        cost.setSupplierPrice(BigDecimal.valueOf(dto.getSupplierPrice()));
        cost.setRetailPrice(BigDecimal.valueOf(dto.getSalePrice()));
        cost.setPlu(dto.getPlu());
        Cost savedCost = costRepository.save(cost);

        return new ProductDTO(savedProduct, savedCost);
    }


    public Page<ProductDTO> getProducts(Pageable pageable, String keyword, Integer categoryId) {
        Page<Product> products = productRepository.findFilteredAndSorted(keyword, categoryId, pageable);

        return products.map(product -> {
            Cost cost = costRepository.findByProduct(product).stream().findFirst().orElse(null);
            return new ProductDTO(product, cost);
        });
    }

    public ProductResponseDTO getProductsWithCategoryCounts(Pageable pageable, String keyword, Integer categoryId) {
        Page<ProductDTO> productPage = getProducts(pageable, keyword, categoryId);
        List<CategoryCountDTO> categoryCounts = productRepository.countProductsByCategory();
        return new ProductResponseDTO(productPage, categoryCounts);
    }

    public ProductDTO updateProduct(Integer id, ProductRequestDTO dto, MultipartFile productImage, MultipartFile ingredientsImage) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // 이미지 새로 업로드했을 경우 처리
        if (productImage != null && !productImage.isEmpty()) {
            FileUtil.UploadResult result = fileUtil.saveImage(productImage, "product");
            product.setImageUrl(result.getUrl());
            product.setImageName(result.getFileName());
        }

        if (ingredientsImage != null && !ingredientsImage.isEmpty()) {
            FileUtil.UploadResult result = fileUtil.saveImage(ingredientsImage, "ingredients");
            product.setIngredientsImageUrl(result.getUrl());
            product.setIngredientsImageName(result.getFileName());
        }

        // 나머지 수정 처리
        product.setProductName(dto.getProductName());
        product.setSupplier(supplierRepository.findById(dto.getSupplierId()).orElseThrow());
        product.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow());
        product.setAllergies(dto.getAllergies());
        product.setPasteurized(dto.getPasteurized());
        product.setDescription(dto.getDescription());
        product.setServingSuggestion(dto.getSuggestion());
        product.setUpdatedAt(LocalDateTime.now());

        if (!product.getProductDetails().isEmpty()) {
            ProductDetail detail = product.getProductDetails().get(0);
            detail.setAnimal(animalRepository.findById(dto.getAnimalId()).orElseThrow());
            detail.setCountry(countryRepository.findById(dto.getOriginId()).orElseThrow());
        }

        productRepository.save(product);

        Cost cost = costRepository.findByProduct(product).stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No cost info"));

        cost.setPriceType(dto.getPriceType());
        cost.setSupplierPrice(BigDecimal.valueOf(dto.getSupplierPrice()));
        cost.setRetailPrice(BigDecimal.valueOf(dto.getSalePrice()));
        cost.setPlu(dto.getPlu());

        costRepository.save(cost);

        return new ProductDTO(product, cost);
    }

    public ProductFormResponseDTO getProductForm(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        Cost cost = costRepository.findByProduct(product)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No cost info for this product"));

        ProductFormResponseDTO dto = new ProductFormResponseDTO();
        dto.setPid(product.getPid());
        dto.setCategoryId(product.getCategory().getCategoryId());
        dto.setProductName(product.getProductName());
        dto.setSupplierId(product.getSupplier().getSid());
        dto.setPriceType(cost.getPriceType().name());
        dto.setSupplierPrice(cost.getSupplierPrice().doubleValue());
        dto.setSalePrice(cost.getRetailPrice().doubleValue());
        dto.setPlu(cost.getPlu());

        if (product.getProductDetails() != null && !product.getProductDetails().isEmpty()) {
            dto.setAnimalId(product.getProductDetails().get(0).getAnimal().getAnimalId());
            dto.setOriginId(product.getProductDetails().get(0).getCountry().getCountryId());
        }

        dto.setPasteurized(product.getPasteurized());
        dto.setAllergies(product.getAllergies());
        dto.setProductImageName(product.getImageName());
        dto.setProductImageUrl(product.getImageUrl());
        dto.setIngredientsImageName(product.getIngredientsImageName());
        dto.setIngredientsImageUrl(product.getIngredientsImageUrl());
        dto.setDescription(product.getDescription());
        dto.setSuggestion(product.getServingSuggestion());

        return dto;
    }

    // ✅ Customer - 상품 전체 조회 (카테고리+검색)
    public Page<CustomerProductListDTO> getProductsForCustomer(Pageable pageable, String category, String keyword) {
        Page<Product> products = productRepository.findForCustomerSorted(keyword, category, pageable);

        return products.map(product -> {
            CustomerProductListDTO dto = new CustomerProductListDTO();
            dto.setPid(product.getPid());
            dto.setProductImageName(product.getImageName());
            dto.setProductImageUrl(product.getImageUrl());
            dto.setProductName(product.getProductName());
            dto.setOriginName(product.getProductDetails().isEmpty() ? null : product.getProductDetails().get(0).getCountry().getCountryName());
            dto.setCategoryName(product.getCategory().getCategoryName());
            dto.setAnimalName(product.getProductDetails().isEmpty() ? null : product.getProductDetails().get(0).getAnimal().getAnimalName());
            dto.setPasteurized(product.getPasteurized());
            dto.setAllergies(product.getAllergies());
            return dto;
        });
    }

    // ✅ Customer - 상품 상세 조회 (id 기준)
    public CustomerProductDTO getCustomerProductDetail(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        CustomerProductDTO dto = new CustomerProductDTO();
        dto.setProductName(product.getProductName());
        dto.setOriginName(product.getProductDetails().isEmpty() ? null : product.getProductDetails().get(0).getCountry().getCountryName());
        dto.setAllergies(product.getAllergies());
        dto.setDescription(product.getDescription());
        dto.setPasteurized(product.getPasteurized());
        dto.setServingSuggestion(product.getServingSuggestion());
        dto.setIngredientsImageName(product.getIngredientsImageName());
        dto.setIngredientsImageUrl(product.getIngredientsImageUrl());

        return dto;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public List<Country> getAllOrigins() {
        return countryRepository.findAll();
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @Transactional
    public void updateInStock(Integer productId, boolean inStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setInStock(inStock);
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
    }

}
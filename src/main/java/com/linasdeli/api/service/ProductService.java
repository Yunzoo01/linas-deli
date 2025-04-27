package com.linasdeli.api.service;

import com.linasdeli.api.domain.*;
import com.linasdeli.api.domain.enums.PriceType;
import com.linasdeli.api.dto.CategoryCountDTO;
import com.linasdeli.api.dto.ProductListItemDTO;
import com.linasdeli.api.dto.request.ProductRequestDTO;
import com.linasdeli.api.dto.ProductDTO;
import com.linasdeli.api.dto.response.CustomerProductDTO;
import com.linasdeli.api.dto.response.CustomerProductListDTO;
import com.linasdeli.api.dto.response.ProductFormResponseDTO;
import com.linasdeli.api.dto.response.ProductResponseDTO;
import com.linasdeli.api.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public ProductService(ProductRepository productRepository,
                          SupplierRepository supplierRepository,
                          CategoryRepository categoryRepository,
                          AnimalRepository animalRepository,
                          CountryRepository countryRepository,
                          CostRepository costRepository,
                          ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.categoryRepository = categoryRepository;
        this.animalRepository = animalRepository;
        this.countryRepository = countryRepository;
        this.costRepository = costRepository;
        this.modelMapper = modelMapper;
    }

    public ProductDTO createProduct(ProductRequestDTO dto) {
        // 🔸 연관 엔티티 조회
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Animal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid animal ID"));

        Country country = countryRepository.findById(dto.getOriginId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid origin ID"));

        // 🔸 Product 생성
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setSupplier(supplier);
        product.setCategory(category);
        product.setAllergies(dto.getAllergies());
        product.setPasteurized(dto.getPasteurized());
        product.setImageName(dto.getProductImageName());
        product.setImageUrl(dto.getProductImageUrl());
        product.setIngredientsImageName(dto.getIngredientsImageName());
        product.setIngredientsImageUrl(dto.getIngredientsImageUrl());
        product.setDescription(dto.getDescription());
        product.setServingSuggestion(dto.getSuggestion());
        product.setInStock(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // 🔸 ProductDetail 생성
        ProductDetail detail = new ProductDetail();
        detail.setProduct(product);
        detail.setAnimal(animal);
        detail.setCountry(country);
        product.setProductDetails(List.of(detail));

        // 🔸 저장 (먼저 Product)
        Product savedProduct = productRepository.save(product);

        // 🔸 Cost 생성 및 저장
        Cost cost = new Cost();
        cost.setProduct(savedProduct);
        cost.setPriceType(dto.getPriceType());
        cost.setSupplierPrice(BigDecimal.valueOf(dto.getSupplierPrice()));
        cost.setRetailPrice(BigDecimal.valueOf(dto.getSalePrice()));
        cost.setPlu(dto.getPlu());

        Cost savedCost = costRepository.save(cost);

        // ✅ 최종 응답 DTO로 반환
        log.info("Product created: {}", savedProduct.getProductName());
        return new ProductDTO(savedProduct, savedCost);
    }

    public Page<ProductDTO> getProducts(Pageable pageable, String keyword, String category) {
        Page<Product> products;

        if ((keyword == null || keyword.isEmpty()) && (category == null || category.isEmpty() || category.equalsIgnoreCase("All"))) {
            products = productRepository.findAll(pageable);
        } else if (category == null || category.isEmpty()) {
            products = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
        } else if (keyword == null || keyword.isEmpty()) {
            products = productRepository.findByCategory_CategoryNameIgnoreCase(category, pageable);
        } else {
            products = productRepository.findByProductNameContainingIgnoreCaseAndCategory_CategoryNameIgnoreCase(keyword, category, pageable);
        }

        return products.map(product -> {
            Cost cost = costRepository.findByProduct(product).stream().findFirst().orElse(null);
            return new ProductDTO(product, cost);
        });
    }

    public List<ProductListItemDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> {
            ProductListItemDTO dto = new ProductListItemDTO();
            dto.setPid(product.getPid());
            dto.setProductName(product.getProductName());
            dto.setProductImageName(product.getImageName());
            dto.setProductImageUrl(product.getImageUrl());
            dto.setInStock(product.isInStock());

            List<Cost> costs = costRepository.findByProduct(product);
            if (!costs.isEmpty()) {
                Cost cost = costs.get(0);
                dto.setPlu(cost.getPlu() != null ? String.valueOf(cost.getPlu()) : null);
            } else {
                dto.setPlu(null);
            }

            return dto;
        }).toList();
    }
    public ProductResponseDTO getProductsWithCategoryCounts(Pageable pageable, String keyword, String category) {
        Page<ProductDTO> productPage = getProducts(pageable, keyword, category);
        List<CategoryCountDTO> categoryCounts = productRepository.countProductsByCategory();
        return new ProductResponseDTO(productPage, categoryCounts);
    }

    public ProductDTO updateProduct(Integer id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        // product 수정
        product.setProductName(dto.getProductName());
        product.setSupplier(supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID")));
        product.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID")));
        product.setAllergies(dto.getAllergies());
        product.setPasteurized(dto.getPasteurized());
        product.setImageName(dto.getProductImageName());
        product.setImageUrl(dto.getProductImageUrl());
        product.setIngredientsImageName(dto.getIngredientsImageName());
        product.setIngredientsImageUrl(dto.getIngredientsImageUrl());
        product.setDescription(dto.getDescription());
        product.setServingSuggestion(dto.getSuggestion());
        product.setUpdatedAt(LocalDateTime.now());

        // Detail 수정
        if (product.getProductDetails() != null && !product.getProductDetails().isEmpty()) {
            ProductDetail detail = product.getProductDetails().get(0);
            detail.setAnimal(animalRepository.findById(dto.getAnimalId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid animal ID")));
            detail.setCountry(countryRepository.findById(dto.getOriginId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid country ID")));
        }

        // 저장
        Product updatedProduct = productRepository.save(product);

        // Cost 수정
        Cost cost = costRepository.findByProduct(product)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No cost info for this product"));

        cost.setPriceType(dto.getPriceType());
        cost.setSupplierPrice(BigDecimal.valueOf(dto.getSupplierPrice()));
        cost.setRetailPrice(BigDecimal.valueOf(dto.getSalePrice()));
        cost.setPlu(dto.getPlu());

        costRepository.save(cost);

        return new ProductDTO(updatedProduct, cost);
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
        Page<Product> products;

        if ((keyword == null || keyword.isEmpty()) && (category == null || category.isEmpty() || category.equalsIgnoreCase("All"))) {
            products = productRepository.findAll(pageable);
        } else if (category == null || category.isEmpty()) {
            products = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
        } else if (keyword == null || keyword.isEmpty()) {
            products = productRepository.findByCategory_CategoryNameIgnoreCase(category, pageable);
        } else {
            products = productRepository.findByProductNameContainingIgnoreCaseAndCategory_CategoryNameIgnoreCase(keyword, category, pageable);
        }

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

}
package com.linasdeli.api.service;

import com.linasdeli.api.domain.*;
import com.linasdeli.api.domain.enums.PriceType;
import com.linasdeli.api.dto.CategoryCountDTO;
import com.linasdeli.api.dto.ProductListItemDTO;
import com.linasdeli.api.dto.request.ProductRequestDTO;
import com.linasdeli.api.dto.ProductDTO;
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
}
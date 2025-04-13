package com.linasdeli.api.service;

import com.linasdeli.api.domain.Cost;
import com.linasdeli.api.domain.Product;
import com.linasdeli.api.domain.ProductDetail;
import com.linasdeli.api.domain.enums.AllergyType;
import com.linasdeli.api.domain.enums.PriceType;
import com.linasdeli.api.dto.request.ProductFormRequestDTO;
import com.linasdeli.api.dto.response.ProductFormResponseDTO;
import com.linasdeli.api.dto.response.ProductListResponseDTO;
import com.linasdeli.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    private final CostRepository costRepository;
    private final AnimalRepository animalRepository;
    private final CountryRepository countryRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductFormResponseDTO createProduct(ProductFormRequestDTO dto) {
        //DTO에 들어있는거 다 매핑되는거임. 그래서 product에서 supplier같이 아이디로 되어있는거 다 set해줘여되고, 없는거 다 set해줘야됨. aleergy도 enum이니까.
        Product product = modelMapper.map(dto, Product.class);

        product.setSupplier(supplierRepository.findById(dto.getSupplierId()).orElseThrow());
        product.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow());
        List<AllergyType> allergyList = dto.getAllergy().stream()
                .map(AllergyType::valueOf)
                .toList();
        product.setAllergies(allergyList);
        product.setInStock(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setPasteurized(dto.getPasteurized());

        productRepository.save(product);

        Cost cost = new Cost();
        cost.setProduct(product);
        cost.setPriceType(PriceType.valueOf(dto.getPriceType()));
        cost.setSupplierPrice(dto.getSupplierPrice());
        cost.setDivisor(dto.getDivisor());
        cost.setRetailPrice(dto.getRetailPrice());
        cost.setPlu(dto.getPlu());
        costRepository.save(cost);

        ProductDetail detail = new ProductDetail();
        detail.setProduct(product);
        detail.setAnimal(animalRepository.findById(dto.getAnimalId()).orElseThrow());
        detail.setCountry(countryRepository.findById(dto.getCountryId()).orElseThrow());
        productDetailRepository.save(detail);

        return ProductFormResponseDTO.builder()
                .productId(product.getPid())
                .productName(product.getProductName())
                .categoryId(product.getCategory().getCategoryId())
                .supplierId(product.getSupplier().getSid())
                .retailPrice(cost.getRetailPrice())
                .priceType(cost.getPriceType().name())
                .imageUrl(product.getImageUrl())
                .build();
    }

    //디테일에서 조회할때...
    @Override
    public ProductFormResponseDTO getProductById(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        Cost cost = costRepository.findByProduct(product).stream().findFirst().orElse(null);
        return ProductFormResponseDTO.builder()
                .productId(product.getPid())
                .productName(product.getProductName())
                .categoryId(product.getCategory().getCategoryId())
                .supplierId(product.getSupplier().getSid())
                .imageUrl(product.getImageUrl())
                .imageName(product.getImageName())
                .ingredientsImageUrl(product.getIngredientsImageUrl())
                .ingredientsImageName(product.getIngredientsImageName())
                .description(product.getDescription())
                .servingSuggestion(product.getServingSuggestion())
                .allergy(product.getAllergies() != null
                        ? product.getAllergies().stream().map(Enum::name).toList()
                        : null)
                .pasteurized(product.getPasteurized())
                .priceType(cost != null ? cost.getPriceType().name() : null)
                .supplierPrice(cost != null ? cost.getSupplierPrice() : null)
                .divisor(cost != null ? cost.getDivisor() : null)
                .retailPrice(cost != null ? cost.getRetailPrice() : null)
                .plu(cost != null ? cost.getPlu() : null)
//                .animalId(detail != null ? detail.getAnimal().getAnimalId() : null)
//                .countryId(detail != null ? detail.getCountry().getCountryId() : null)
                .build();
    }

    //업데이트
    @Override
    public ProductFormResponseDTO updateProduct(Integer id, ProductFormRequestDTO dto) {
        // 1. Product 조회
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

// 2. 기본 필드 수정
        product.setProductName(dto.getProductName());
        product.setImageUrl(dto.getImageUrl());
        product.setImageName(dto.getImageName());
        product.setIngredientsImageUrl(dto.getIngredientsImageUrl());
        product.setIngredientsImageName(dto.getIngredientsImageName());
        product.setDescription(dto.getDescription());
        product.setServingSuggestion(dto.getServingSuggestion());
        product.setPasteurized(dto.getPasteurized());
        if (dto.getAllergy() != null) {
            List<AllergyType> allergyList = dto.getAllergy().stream()
                    .map(AllergyType::valueOf)
                    .toList();
            product.setAllergies(new ArrayList<>(allergyList));
        }
        product.setSupplier(supplierRepository.findById(dto.getSupplierId()).orElseThrow());
        product.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow());
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);

// 3. 💡 Cost가 있을 때만 수정
        Cost cost = costRepository.findByProduct(product).stream().findFirst().orElse(null);
        if (cost != null) {
            cost.setPriceType(PriceType.valueOf(dto.getPriceType()));
            cost.setSupplierPrice(dto.getSupplierPrice());
            cost.setDivisor(dto.getDivisor());
            cost.setRetailPrice(dto.getRetailPrice());
            cost.setPlu(dto.getPlu());
            costRepository.save(cost);
        }

// 4. 💡 ProductDetail도 있을 때만 수정
        ProductDetail detail = productDetailRepository.findAll().stream()
                .filter(d -> d.getProduct().getPid().equals(product.getPid()))
                .findFirst().orElse(null);

        if (detail != null) {
            detail.setAnimal(animalRepository.findById(dto.getAnimalId()).orElseThrow());
            detail.setCountry(countryRepository.findById(dto.getCountryId()).orElseThrow());
            productDetailRepository.save(detail);
        }

// 5. 응답
        return ProductFormResponseDTO.builder()
                .productId(product.getPid())
                .productName(product.getProductName())
                .categoryId(product.getCategory().getCategoryId())
                .supplierId(product.getSupplier().getSid())
                .imageUrl(product.getImageUrl())
                .imageName(product.getImageName())
                .ingredientsImageUrl(product.getIngredientsImageUrl())
                .ingredientsImageName(product.getIngredientsImageName())
                .description(product.getDescription())
                .servingSuggestion(product.getServingSuggestion())
                .allergy(product.getAllergies() != null
                        ? product.getAllergies().stream().map(Enum::name).toList()
                        : null)
                .pasteurized(product.getPasteurized())
                .priceType(cost != null ? cost.getPriceType().name() : null)
                .supplierPrice(cost != null ? cost.getSupplierPrice() : null)
                .divisor(cost != null ? cost.getDivisor() : null)
                .retailPrice(cost != null ? cost.getRetailPrice() : null)
                .plu(cost != null ? cost.getPlu() : null)
                .animalId(detail != null ? detail.getAnimal().getAnimalId() : null)
                .countryId(detail != null ? detail.getCountry().getCountryId() : null)
                .build();
    }

    //staff 페이지에서 전체 리스트 조회할때
    @Override
    public List<ProductListResponseDTO> getAllProductsForList() {
        return productRepository.findAll().stream()
                .map(product -> ProductListResponseDTO.builder()
                        .productId(product.getPid())
                        .imageUrl(product.getImageUrl())
                        .categoryId(product.getCategory().getCategoryId())
                        .productName(product.getProductName())
                        .instock(product.isInStock())
                        .build())
                .toList();
    }
}
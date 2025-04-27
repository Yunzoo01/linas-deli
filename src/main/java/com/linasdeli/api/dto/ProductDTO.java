package com.linasdeli.api.dto;

import com.linasdeli.api.domain.Cost;
import com.linasdeli.api.domain.Product;
import com.linasdeli.api.domain.enums.AllergyType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO {
    private Integer pid;
    private String productName;
    private String supplierName;
    private Integer supplierId;
    private String categoryName;
    private Integer categoryId;

    private String priceType;
    private Double supplierPrice;
    private Double retailPrice;

    private Integer plu;

    private Integer animalId;
    private String animalName;

    private Boolean pasteurized;
    private Integer originId;
    private String originName;

    private List<AllergyType> allergies;

    private String productImageName;
    private String productImageUrl;
    private String ingredientsImageName;
    private String ingredientsImageUrl;

    private String description;
    private String suggestion;

    private boolean inStock;

    public ProductDTO() {
    }

    public ProductDTO(Product product, Cost cost) {
        this.pid = product.getPid();
        this.productName = product.getProductName();

        this.supplierId = product.getSupplier().getSid();
        this.supplierName = product.getSupplier().getSupplierName();

        this.categoryId = product.getCategory().getCategoryId();
        this.categoryName = product.getCategory().getCategoryName();

        if (cost != null) {
            this.priceType = cost.getPriceType().name();
            this.supplierPrice = cost.getSupplierPrice().doubleValue();
            this.retailPrice = cost.getRetailPrice().doubleValue();
            this.plu = cost.getPlu();
        } else {
            this.priceType = null;
            this.supplierPrice = null;
            this.retailPrice = null;
            this.plu = null;
        }

        if (product.getProductDetails() != null && !product.getProductDetails().isEmpty()) {
            this.animalId = product.getProductDetails().get(0).getAnimal().getAnimalId();
            this.animalName = product.getProductDetails().get(0).getAnimal().getAnimalName();

            this.originId = product.getProductDetails().get(0).getCountry().getCountryId();
            this.originName = product.getProductDetails().get(0).getCountry().getCountryName();
        }

        this.pasteurized = product.getPasteurized();
        this.allergies = product.getAllergies();

        this.productImageName = product.getImageName();
        this.productImageUrl = product.getImageUrl();
        this.ingredientsImageName = product.getIngredientsImageName();
        this.ingredientsImageUrl = product.getIngredientsImageUrl();

        this.description = product.getDescription();
        this.suggestion = product.getServingSuggestion();

        this.inStock = product.isInStock();
    }
}
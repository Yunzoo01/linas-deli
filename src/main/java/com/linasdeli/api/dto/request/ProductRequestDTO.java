package com.linasdeli.api.dto.request;

import com.linasdeli.api.domain.enums.AllergyType;
import com.linasdeli.api.domain.enums.PriceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequestDTO {
    private Integer pid;

    @NotNull(message = "Category is required")
    private Integer categoryId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotNull(message = "Supplier is required")
    private Integer supplierId;

    @NotNull(message = "Price type is required")
    private PriceType priceType;

    private Double supplierPrice;
    private Double salePrice;

    private Integer plu;

    private Integer animalId;
    private Boolean pasteurized;
    private Integer originId;

    private List<AllergyType> allergies;

    private String productImageName;
    private String productImageUrl;
    private String ingredientsImageName;
    private String ingredientsImageUrl;

    private String description;
    private String suggestion;

    public ProductRequestDTO(Integer pid, Integer categoryId, String productName, Integer supplierId, PriceType priceType,
                             Double supplierPrice, Double salePrice, Integer plu, Integer animalId, Boolean pasteurized, Integer originId,
                             List<AllergyType> allergies, String productImageName, String productImageUrl,
                             String ingredientsImageName, String ingredientsImageUrl, String description, String suggestion) {
        this.pid = pid;
        this.categoryId = categoryId;
        this.productName = productName;
        this.supplierId = supplierId;
        this.priceType = priceType;
        this.supplierPrice = supplierPrice;
        this.salePrice = salePrice;
        this.plu = plu;
        this.animalId = animalId;
        this.pasteurized = pasteurized;
        this.originId = originId;
        this.allergies = allergies;
        this.productImageName = productImageName;
        this.productImageUrl = productImageUrl;
        this.ingredientsImageName = ingredientsImageName;
        this.ingredientsImageUrl = ingredientsImageUrl;
        this.description = description;
        this.suggestion = suggestion;
    }
}
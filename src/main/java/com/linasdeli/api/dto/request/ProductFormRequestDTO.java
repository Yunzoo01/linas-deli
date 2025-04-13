package com.linasdeli.api.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductFormRequestDTO {

    private String productName;
    private Integer supplierId;
    private Integer categoryId;
    private String imageUrl;
    private String imageName;
    private String ingredientsImageUrl;
    private String ingredientsImageName;
    private String description;
    private String servingSuggestion;
    private List<String> allergy; // ["G", "L"] 형태로 들어올 수 있게
    private Boolean pasteurized;

    private String priceType; // U or W
    private BigDecimal supplierPrice;
    private BigDecimal divisor;
    private BigDecimal retailPrice;
    private Integer plu;

    private Integer animalId;
    private Integer countryId;
}

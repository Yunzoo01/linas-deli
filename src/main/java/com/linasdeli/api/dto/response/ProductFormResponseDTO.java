package com.linasdeli.api.dto.response;

import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductFormResponseDTO {
    private Integer productId;
    private String productName;
    private Integer supplierId;
    private Integer categoryId;
    private String imageUrl;
    private String imageName;
    private String ingredientsImageUrl;
    private String ingredientsImageName;
    private String description;
    private String servingSuggestion;
    private List<String> allergy;
    private Boolean pasteurized;

    private String priceType;
    private BigDecimal supplierPrice;
    private BigDecimal divisor;
    private BigDecimal retailPrice;
    private Integer plu;

    private Integer animalId;
    private Integer countryId;
}
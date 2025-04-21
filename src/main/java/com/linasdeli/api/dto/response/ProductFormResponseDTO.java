package com.linasdeli.api.dto.response;

import com.linasdeli.api.domain.enums.AllergyType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductFormResponseDTO {
    private Integer pid;
    private Integer categoryId;
    private String productName;
    private Integer supplierId;
    private String priceType;
    private Double cost;
    private Double salePrice;
    private String plu;

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
}
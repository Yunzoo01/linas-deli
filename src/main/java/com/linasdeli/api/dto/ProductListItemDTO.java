package com.linasdeli.api.dto;

import lombok.*;

@Getter
@Setter
public class ProductListItemDTO {
    private Integer pid;
    private String productName;
    private String productImageName;
    private String productImageUrl;
    private String plu;
    private boolean inStock;
}
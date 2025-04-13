package com.linasdeli.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductListResponseDTO {
    private Integer productId;
    private String imageUrl;
    private Integer categoryId;
    private String productName;
    private boolean instock; // true if inStock == false
}
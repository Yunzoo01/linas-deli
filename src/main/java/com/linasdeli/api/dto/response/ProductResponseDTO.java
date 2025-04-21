package com.linasdeli.api.dto.response;

import com.linasdeli.api.dto.ProductDTO;
import com.linasdeli.api.dto.CategoryCountDTO;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
public class ProductResponseDTO {
    private Page<ProductDTO> productList;
    private List<CategoryCountDTO> categoryCounts;

    public ProductResponseDTO(Page<ProductDTO> productList, List<CategoryCountDTO> categoryCounts) {
        this.productList = productList;
        this.categoryCounts = categoryCounts;
    }

    public Page<ProductDTO> getProductList() { return productList; }
    public List<CategoryCountDTO> getCategoryCounts() { return categoryCounts; }
}
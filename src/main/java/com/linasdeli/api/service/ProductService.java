package com.linasdeli.api.service;

import com.linasdeli.api.domain.Product;
import com.linasdeli.api.dto.request.ProductFormRequestDTO;
import com.linasdeli.api.dto.response.ProductFormResponseDTO;
import com.linasdeli.api.dto.response.ProductListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductFormResponseDTO createProduct(ProductFormRequestDTO dto);
    ProductFormResponseDTO getProductById(Integer productId);
    ProductFormResponseDTO updateProduct(Integer id, ProductFormRequestDTO dto);

    List<ProductListResponseDTO> getAllProductsForList();
}
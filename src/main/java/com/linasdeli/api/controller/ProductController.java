package com.linasdeli.api.controller;

import com.linasdeli.api.dto.response.CustomerProductDTO;
import com.linasdeli.api.dto.response.CustomerProductListDTO;
import com.linasdeli.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 전체 product 목록
    @GetMapping
    public ResponseEntity<List<CustomerProductListDTO>> getCustomerProductList() {
        return ResponseEntity.ok(productService.getCustomerProductList());
    }

    // 상세 product 조회
    @GetMapping("/{id}")
    public ResponseEntity<CustomerProductDTO> getCustomerProductDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getCustomerProductDetail(id));
    }
}

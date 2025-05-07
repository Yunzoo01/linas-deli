package com.linasdeli.api.controller;

import com.linasdeli.api.dto.response.CustomerProductDTO;
import com.linasdeli.api.dto.response.CustomerProductListDTO;
import com.linasdeli.api.dto.response.ProductWithDetailsDto;
import com.linasdeli.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ✅ Customer - 상품 전체 조회 (카테고리 + 검색 필터 포함)
    @GetMapping
    public ResponseEntity<Page<ProductWithDetailsDto>> getProductsForCustomer(
            Pageable pageable,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.info("getProductsForCustomer called"+category);
        Page<ProductWithDetailsDto> products = productService.getProductsForCustomer(pageable, category, keyword);
        return ResponseEntity.ok(products);
    }

    // ✅ Customer - 상품 상세 조회 (id 기준)
    @GetMapping("/{id}")
    public ResponseEntity<CustomerProductDTO> getCustomerProductDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getCustomerProductDetail(id));
    }
}
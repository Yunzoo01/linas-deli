package com.linasdeli.api.controller.staff;

import com.linasdeli.api.dto.CategoryCountDTO;
import com.linasdeli.api.dto.ProductDTO;
import com.linasdeli.api.dto.response.ProductFormResponseDTO;
import com.linasdeli.api.dto.request.ProductRequestDTO;
import com.linasdeli.api.dto.response.ProductResponseDTO;
import com.linasdeli.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff/products")
@RequiredArgsConstructor
public class StaffProductController {

    private final ProductService productService;

    // ✅ 상품 추가
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    // ✅ 상품 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    // ✅ 전체 상품 목록 조회 (리스트 뷰용)
    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<ProductResponseDTO> getProducts(
            Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category
    ) {
        return ResponseEntity.ok(productService.getProductsWithCategoryCounts(pageable, keyword, category));
    }

    // ✅ 수정용 폼 데이터 조회 (폼에 pre-fill용)
    @GetMapping("/{id}")
    public ResponseEntity<ProductFormResponseDTO> getProductForm(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductForm(id));
    }
}
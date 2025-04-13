package com.linasdeli.api.controller.staff;

import com.linasdeli.api.dto.request.ProductFormRequestDTO;
import com.linasdeli.api.dto.response.ProductFormResponseDTO;
import com.linasdeli.api.dto.response.ProductListResponseDTO;
import com.linasdeli.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class StaffProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductFormResponseDTO> create(@RequestBody ProductFormRequestDTO dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductFormResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductFormResponseDTO> update(@PathVariable Integer id, @RequestBody ProductFormRequestDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    //전체조회
    @GetMapping("/list")
    public ResponseEntity<List<ProductListResponseDTO>> getAllProductsForList() {
        return ResponseEntity.ok(productService.getAllProductsForList());
    }

}
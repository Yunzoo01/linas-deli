package com.linasdeli.api.controller.staff;

import com.linasdeli.api.domain.Animal;
import com.linasdeli.api.domain.Category;
import com.linasdeli.api.domain.Country;
import com.linasdeli.api.domain.Supplier;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/staff/products")
@RequiredArgsConstructor
public class StaffProductController {

    private final ProductService productService;

    // ✅ 상품 추가
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @ModelAttribute ProductRequestDTO dto,
            @RequestParam(required = false) MultipartFile productImage,
            @RequestParam(required = false) MultipartFile ingredientsImage) {

        ProductDTO created = productService.createProduct(dto, productImage, ingredientsImage);
        return ResponseEntity.ok(created);
    }

    // ✅ 상품 수정
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Integer id,
            @ModelAttribute ProductRequestDTO dto, // ✔️ @RequestPart 아님!
            @RequestPart(required = false) MultipartFile productImage,
            @RequestPart(required = false) MultipartFile ingredientsImage
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, dto, productImage, ingredientsImage));
    }

    // ✅ 전체 상품 목록 조회 (리스트 뷰용)
    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<ProductResponseDTO> getProducts(
            Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Integer categoryId
    ) {
        return ResponseEntity.ok(productService.getProductsWithCategoryCounts(pageable, keyword, categoryId));
    }

    // ✅ 수정용 폼 데이터 조회 (폼에 pre-fill용)
    @GetMapping("/{id}")
    public ResponseEntity<ProductFormResponseDTO> getProductForm(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductForm(id));
    }

    // ✅ 전체 Supplier 목록 가져오기
    @GetMapping("/suppliers")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        return ResponseEntity.ok(productService.getAllSuppliers());
    }

    // ✅ 전체 Animal 목록 가져오기
    @GetMapping("/animals")
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return ResponseEntity.ok(productService.getAllAnimals());
    }

    // ✅ 전체 Origin (Country) 목록 가져오기
    @GetMapping("/origins")
    public ResponseEntity<List<Country>> getAllOrigins() {
        return ResponseEntity.ok(productService.getAllOrigins());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @PatchMapping("/{id}/instock")
    //@PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<Void> updateInStock(@PathVariable Integer id, @RequestParam boolean inStock) {
        productService.updateInStock(id, inStock);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
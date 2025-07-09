package com.linasdeli.api.controller.staff;

import com.linasdeli.api.dto.SupplierDTO;
import com.linasdeli.api.dto.response.SupplierResponseDTO;
import com.linasdeli.api.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/staff/suppliers")
@RequiredArgsConstructor
public class StaffSupplierController {

    private final SupplierService supplierService;

    // ✅ 전체 조회 (페이징)
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<SupplierResponseDTO> getSuppliers(Pageable pageable) {
        Page<SupplierDTO> suppliers = supplierService.getAllSuppliers(pageable);
        long totalSupplierCount = supplierService.countSuppliers();
        return ResponseEntity.ok(new SupplierResponseDTO(suppliers, totalSupplierCount));
    }

    // ✅ 단일 조회
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Integer id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    // ✅ 등록
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody SupplierDTO dto) {
        return ResponseEntity.ok(supplierService.createSupplier(dto));
    }

    // ✅ 수정
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Integer id, @RequestBody SupplierDTO dto) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, dto));
    }

    // ✅ 삭제
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
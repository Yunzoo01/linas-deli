package com.linasdeli.api.controller.staff;

import com.linasdeli.api.dto.OrderDTO;
import com.linasdeli.api.dto.OrderStatusCountDTO;
import com.linasdeli.api.dto.SupplierDTO;
import com.linasdeli.api.dto.response.OrderResponseDTO;
import com.linasdeli.api.dto.response.SupplierResponseDTO;
import com.linasdeli.api.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/staff/suppliers")
public class StaffSupplierController {

    private final SupplierService supplierService;

    public StaffSupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // ✅ 업체 목록 조회 (페이징 및 검색) - DTO 활용
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<SupplierResponseDTO> getSuppliers(
            Pageable pageable
    ) {
        Page<SupplierDTO> suppliers = supplierService.getAllSuppliers(pageable);
        long totalSupplierCount = supplierService.countSuppliers();

        SupplierResponseDTO response = new SupplierResponseDTO(suppliers,totalSupplierCount);
        return ResponseEntity.ok(response);
    }

}

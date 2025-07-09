package com.linasdeli.api.service;

import com.linasdeli.api.domain.Supplier;
import com.linasdeli.api.dto.OrderDTO;
import com.linasdeli.api.dto.SupplierDTO;
import com.linasdeli.api.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Page<SupplierDTO> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAllSuppliers(pageable).map(SupplierDTO::new);

    }

    public long countSuppliers() {
        return supplierRepository.countTotalSuppliers();
    }

    public SupplierDTO getSupplierById(Integer id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return new SupplierDTO(supplier);
    }

    public SupplierDTO createSupplier(SupplierDTO dto) {
        Supplier supplier = new Supplier();
        supplier.setSupplierName(dto.getSupplierName());
        supplier.setContactName(dto.getContactName());
        supplier.setSEmail(dto.getsEmail());
        supplier.setSPhone(dto.getsPhone());
        supplier.setNote(dto.getNote());

        Supplier saved = supplierRepository.save(supplier);
        return new SupplierDTO(saved);
    }

    public SupplierDTO updateSupplier(Integer id, SupplierDTO dto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setSupplierName(dto.getSupplierName());
        supplier.setContactName(dto.getContactName());
        supplier.setSEmail(dto.getsEmail());
        supplier.setSPhone(dto.getsPhone());
        supplier.setNote(dto.getNote());

        Supplier updated = supplierRepository.save(supplier);
        return new SupplierDTO(updated);
    }

    public void deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
    }

}

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


}

package com.linasdeli.api.repository;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.domain.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    @Query("SELECT s FROM Supplier s ORDER BY s.sid DESC")
    Page<Supplier> findAllSuppliers(Pageable pageable);

    //전체 업체 개수
    @Query("SELECT COUNT(s) FROM Supplier s")
    long countTotalSuppliers();
}
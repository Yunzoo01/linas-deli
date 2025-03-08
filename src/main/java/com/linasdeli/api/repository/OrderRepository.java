package com.linasdeli.api.repository;

import com.linasdeli.api.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends
        JpaRepository<Order, Long> {
    Page<Order> findByEmailContainingIgnoreCaseOrderByOidDesc(String keyword, Pageable pageable);
}

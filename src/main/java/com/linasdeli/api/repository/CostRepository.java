package com.linasdeli.api.repository;

import com.linasdeli.api.domain.Cost;
import com.linasdeli.api.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Integer> {
    List<Cost> findByProduct(Product product); // Product 기준으로 Cost 조회
}
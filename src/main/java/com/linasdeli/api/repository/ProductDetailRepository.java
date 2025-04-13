package com.linasdeli.api.repository;

import com.linasdeli.api.domain.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
}
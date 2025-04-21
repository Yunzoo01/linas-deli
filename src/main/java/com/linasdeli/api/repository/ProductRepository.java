package com.linasdeli.api.repository;

import com.linasdeli.api.domain.Product;
import com.linasdeli.api.dto.CategoryCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategory_CategoryNameIgnoreCase(String category, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCaseAndCategory_CategoryNameIgnoreCase(String keyword, String category, Pageable pageable);
    @Query("SELECT new com.linasdeli.api.dto.CategoryCountDTO(p.category.categoryName, COUNT(p)) " +
            "FROM Product p GROUP BY p.category.categoryName")
    List<CategoryCountDTO> countProductsByCategory();
}
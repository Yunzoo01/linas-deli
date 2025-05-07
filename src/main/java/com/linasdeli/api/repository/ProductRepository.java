package com.linasdeli.api.repository;

import com.linasdeli.api.domain.Product;
import com.linasdeli.api.dto.CategoryCountDTO;
import com.linasdeli.api.dto.response.ProductWithDetailsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 🔹 검색어 + 카테고리 필터 + inStock 정렬 포함된 메인 쿼리
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:categoryId IS NULL OR p.category.categoryId = :categoryId) " +
            "ORDER BY p.inStock DESC, p.productName ASC")
    Page<Product> findFilteredAndSorted(String keyword, Integer categoryId, Pageable pageable);

    // 🔹 카테고리별 상품 수 집계용
    @Query("SELECT new com.linasdeli.api.dto.CategoryCountDTO(p.category.categoryName, COUNT(p)) " +
            "FROM Product p GROUP BY p.category.categoryName")
    List<CategoryCountDTO> countProductsByCategory();

    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:category IS NULL OR LOWER(p.category.categoryName) = LOWER(:category)) " +
            "ORDER BY p.inStock DESC, p.productName ASC")
    Page<Product> findForCustomerSorted(@Param("keyword") String keyword,
                                        @Param("category") String category,
                                        Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.category c " +
            "LEFT JOIN FETCH p.productDetails pd " +
            "LEFT JOIN FETCH pd.animal a " +
            "LEFT JOIN FETCH pd.country co " +
            "WHERE (:keyword IS NULL OR p.productName LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "AND (:category IS NULL OR c.categoryName = :category)")
    Page<Product> searchProducts(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);






}
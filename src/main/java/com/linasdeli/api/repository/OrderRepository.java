package com.linasdeli.api.repository;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.dto.OrderStatusCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface OrderRepository extends
        JpaRepository<Order, Long> {
    // 이메일 검색 (기존)
    Page<Order> findByEmailContainingIgnoreCaseOrderByOidDesc(String keyword, Pageable pageable);

    // 이메일 + 상태로 검색 (단일 상태)
    Page<Order> findByEmailContainingIgnoreCaseAndStatusOrderByOidDesc(String keyword, String status, Pageable pageable);

    Page<Order> findByStatusContainingIgnoreCaseOrderByOidDesc(String status, Pageable pageable);

    @Query("SELECT o FROM Order o ORDER BY o.oid DESC")
    Page<Order> findAllOrders(Pageable pageable);

    // status 별로 주문 갯수 보여주기
    @Query("SELECT new com.linasdeli.api.dto.OrderStatusCountDTO(o.status, COUNT(o)) FROM Order o GROUP BY o.status")
    List<OrderStatusCountDTO> countOrdersByStatus();
    //전체 개시글 개수
    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();
}

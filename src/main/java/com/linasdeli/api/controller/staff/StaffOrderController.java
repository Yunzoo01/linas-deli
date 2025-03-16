package com.linasdeli.api.controller.staff;

import com.linasdeli.api.dto.OrderStatusCountDTO;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.dto.response.OrderDTO;
import com.linasdeli.api.dto.response.OrderResponseDTO;
import com.linasdeli.api.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/staff/orders")
public class StaffOrderController {

    private final OrderService orderService;

    public StaffOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ 오더 목록 조회 (페이징 및 검색) - DTO 활용
    @GetMapping
    public ResponseEntity<OrderResponseDTO> getOrders(
            Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status
            ) {

        Page<OrderDTO> orders = orderService.getOrders(pageable, keyword, status);
        List<OrderStatusCountDTO> orderStatusCount = orderService.countOrdersByStatus();

        OrderResponseDTO response = new OrderResponseDTO(orders,orderStatusCount);
        return ResponseEntity.ok(response);

    }

    // ✅ 주문 조회 (GET) - ID 기반 DTO 변환
//    @GetMapping("/{id}")
//    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
//        return orderService.getOrderById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // ✅ 주문 수정 (PUT) - DTO 활용
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderRequestDTO);
        return ResponseEntity.ok(updatedOrder);
    }
//
//    // ✅ 주문 삭제 (DELETE)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        orderService.deleteOrder(id);
//        return ResponseEntity.ok().build();
//    }
}

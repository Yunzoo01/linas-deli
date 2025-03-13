package com.linasdeli.api.controller.staff;

import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.dto.response.OrderResponseDTO;
import com.linasdeli.api.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(
            Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword) {
        Page<OrderResponseDTO> orders = orderService.getOrders(pageable, keyword);
        return ResponseEntity.ok(orders);
    }

    // ✅ 주문 조회 (GET) - ID 기반 DTO 변환
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ 주문 수정 (PUT) - DTO 활용
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO updatedOrder = orderService.updateOrder(id, orderRequestDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    // ✅ 주문 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}

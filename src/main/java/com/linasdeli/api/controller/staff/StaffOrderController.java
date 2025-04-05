package com.linasdeli.api.controller.staff;

import com.linasdeli.api.dto.OrderDTO;
import com.linasdeli.api.dto.OrderStatusCountDTO;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.dto.response.OrderResponseDTO;
import com.linasdeli.api.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<String> staffTestEndpoint() {
        return ResponseEntity.ok("Staff access granted!");
    }

    // ✅ 오더 목록 조회 (페이징 및 검색) - DTO 활용
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<OrderResponseDTO> getOrders(
            Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status
            ) {
        log.info("getOrders called");
        Page<OrderDTO> orders = orderService.getOrders(pageable, keyword, status);
        List<OrderStatusCountDTO> orderStatusCount = orderService.countOrdersByStatus();

        OrderResponseDTO response = new OrderResponseDTO(orders,orderStatusCount);
        return ResponseEntity.ok(response);

    }

//    // ✅ 주문 수정 (PUT) - DTO 활용
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderRequestDTO);
        return ResponseEntity.ok(updatedOrder);
    }

}

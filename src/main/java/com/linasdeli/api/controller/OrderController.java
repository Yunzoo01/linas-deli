package com.linasdeli.api.controller;

import com.linasdeli.api.dto.OrderDTO;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequestDTO order) {
        log.info("Creating order: {}", order);
        OrderDTO createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }
}

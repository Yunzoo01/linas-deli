package com.linasdeli.api.controller;

import com.linasdeli.api.dto.OrderDTO;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.service.EmailService;
import com.linasdeli.api.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final EmailService emailService;

    @Value("${admin.email}")
    private String adminEmail;

    public OrderController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequest) {
        log.info("Creating order: {}", orderRequest);
        OrderDTO createdOrder = orderService.createOrder(orderRequest);

        return ResponseEntity.ok(createdOrder);
    }
}
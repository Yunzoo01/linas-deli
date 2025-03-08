package com.linasdeli.api.service;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.dto.response.OrderResponseDTO;
import com.linasdeli.api.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ✅ 주문 생성 (Create) - DTO 활용
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setCustomerName(orderRequestDTO.getCustomerName());
        order.setEmail(orderRequestDTO.getEmail());

        Order savedOrder = orderRepository.save(order);
        return new OrderResponseDTO(savedOrder);
    }

    // ✅ Order 목록 페이징 및 검색 (DTO 변환)
    public Page<OrderResponseDTO> getOrders(Pageable pageable, String keyword) {
        Page<Order> orders = (keyword == null || keyword.isEmpty())
                ? orderRepository.findAll(pageable)
                : orderRepository.findByEmailContainingIgnoreCaseOrderByOidDesc(keyword, pageable);

        return orders.map(OrderResponseDTO::new);
    }

    // ✅ 주문 조회 (Read) - DTO 활용
    public Optional<OrderResponseDTO> getOrderById(Long id) {
        return orderRepository.findById(id).map(OrderResponseDTO::new);
    }

    // ✅ 주문 수정 (Update) - DTO 활용
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setCustomerName(orderRequestDTO.getCustomerName());
                    order.setEmail(orderRequestDTO.getEmail());

                    Order updatedOrder = orderRepository.save(order);
                    return new OrderResponseDTO(updatedOrder);
                }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ 주문 삭제 (Delete)
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}

package com.linasdeli.api.service;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    // ✅ 주문 생성 (Create)
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    // ✅ 주문 조회 (Read)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // ✅ 주문 수정 (Update)
    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setCustomerName(updatedOrder.getCustomerName());
                    order.setPhone(updatedOrder.getPhone());
                    order.setEmail(updatedOrder.getEmail());
                    order.setMessage(updatedOrder.getMessage());
                    return orderRepository.save(order);
                }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ 주문 삭제 (Delete)
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}

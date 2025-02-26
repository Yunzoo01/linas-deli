package com.linasdeli.api.service;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("🧪Business Logic - Order")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("✅Create Order test")
    void testCreateOrder() {
        //Given
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setPhone("123-456-7890");
        order.setEmail("john@example.com");

        //When
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        //Then
        assertNotNull(createdOrder);
        assertEquals("John Doe", createdOrder.getCustomerName());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("✅Search Order Test(by Order id)")
    void testGetOrderById() {
        //Given
        Order order = new Order();
        order.setOid(1);
        order.setCustomerName("John Doe");

        //When
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderService.getOrderById(1L);

        //Then
        assertTrue(foundOrder.isPresent());
        assertEquals("John Doe", foundOrder.get().getCustomerName());
        verify(orderRepository, times(1)).findById(1L);
    }


    @Test
    @DisplayName("✅Update Order Test")
    void testUpdateOrder() {
        //Given
        Order existingOrder = new Order();
        existingOrder.setOid(1);
        existingOrder.setCustomerName("Old Name");

        Order updatedOrder = new Order();
        updatedOrder.setCustomerName("New Name");

        //When
        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        Order result = orderService.updateOrder(1L, updatedOrder);

        //Then
        assertNotNull(result);
        assertEquals("New Name", result.getCustomerName());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("✅Delete Order Test")
    void testDeleteOrder() {
        //Given
        Long orderId = 1L;
        doNothing().when(orderRepository).deleteById(orderId);

        //When
        orderService.deleteOrder(orderId);

        //Then
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}

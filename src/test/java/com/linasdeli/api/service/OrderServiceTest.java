package com.linasdeli.api.service;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.dto.response.OrderResponseDTO;
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

@DisplayName("🧪 Business Logic - Order")
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
    @DisplayName("✅ Create Order Test")
    void testCreateOrder() {
        // Given
        OrderRequestDTO requestDTO = new OrderRequestDTO("John Doe", "john@example.com", "Deluxe Platter");
        Order order = new Order();
        order.setCustomerName(requestDTO.getCustomerName());
        order.setEmail(requestDTO.getEmail());

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderResponseDTO createdOrder = orderService.createOrder(requestDTO);

        // Then
        assertNotNull(createdOrder);
        assertEquals("John Doe", createdOrder.getCustomerName());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("✅ Search Order Test (by Order id)")
    void testGetOrderById() {
        // Given
        Order order = new Order();
        order.setOid(1L);
        order.setCustomerName("John Doe");


        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        Optional<OrderResponseDTO> foundOrder = orderService.getOrderById(1L);

        // Then
        assertTrue(foundOrder.isPresent());
        assertEquals("John Doe", foundOrder.get().getCustomerName());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("✅ Update Order Test")
    void testUpdateOrder() {
        // Given
        Order existingOrder = new Order();
        existingOrder.setOid(1L);
        existingOrder.setCustomerName("Old Name");

        OrderRequestDTO updateDTO = new OrderRequestDTO("New Name",  "new@example.com", "Deluxe Platter");
        Order updatedOrder = new Order();
        updatedOrder.setCustomerName(updateDTO.getCustomerName());
        updatedOrder.setEmail(updateDTO.getEmail());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // When
        OrderResponseDTO result = orderService.updateOrder(1L, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("New Name", result.getCustomerName());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("✅ Delete Order Test")
    void testDeleteOrder() {
        // Given
        Long orderId = 1L;
        doNothing().when(orderRepository).deleteById(orderId);

        // When
        orderService.deleteOrder(orderId);

        // Then
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}

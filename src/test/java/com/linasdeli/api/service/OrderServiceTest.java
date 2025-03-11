package com.linasdeli.api.service;

import com.linasdeli.api.domain.Order;
<<<<<<< Updated upstream
=======
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.dto.response.OrderResponseDTO;
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
@DisplayName("🧪Business Logic - Order")
=======
@DisplayName("🧪 Business Logic - Order")
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
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
=======
    @DisplayName("✅ Create Order Test")
    void testCreateOrder() {
        // Given
        OrderRequestDTO requestDTO = new OrderRequestDTO("John Doe", "john@example.com");
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
>>>>>>> Stashed changes
        assertTrue(foundOrder.isPresent());
        assertEquals("John Doe", foundOrder.get().getCustomerName());
        verify(orderRepository, times(1)).findById(1L);
    }

<<<<<<< Updated upstream

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
=======
    @Test
    @DisplayName("✅ Update Order Test")
    void testUpdateOrder() {
        // Given
        Order existingOrder = new Order();
        existingOrder.setOid(1L);
        existingOrder.setCustomerName("Old Name");

        OrderRequestDTO updateDTO = new OrderRequestDTO("New Name",  "new@example.com");
        Order updatedOrder = new Order();
        updatedOrder.setCustomerName(updateDTO.getCustomerName());
        updatedOrder.setEmail(updateDTO.getEmail());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // When
        OrderResponseDTO result = orderService.updateOrder(1L, updateDTO);

        // Then
>>>>>>> Stashed changes
        assertNotNull(result);
        assertEquals("New Name", result.getCustomerName());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
<<<<<<< Updated upstream
    @DisplayName("✅Delete Order Test")
    void testDeleteOrder() {
        //Given
        Long orderId = 1L;
        doNothing().when(orderRepository).deleteById(orderId);

        //When
        orderService.deleteOrder(orderId);

        //Then
=======
    @DisplayName("✅ Delete Order Test")
    void testDeleteOrder() {
        // Given
        Long orderId = 1L;
        doNothing().when(orderRepository).deleteById(orderId);

        // When
        orderService.deleteOrder(orderId);

        // Then
>>>>>>> Stashed changes
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}

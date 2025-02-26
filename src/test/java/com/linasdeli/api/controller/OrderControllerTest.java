package com.linasdeli.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linasdeli.api.domain.Order;
import com.linasdeli.api.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("🧪 REST API - Order Controller")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Changed Mock usage(from 3.4.0 @MockBean -> MockitoBean)
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOid(1);
        order.setCustomerName("John Doe");
    }

    @Test
    @DisplayName("✅ 주문 생성 API 테스트")
    void testCreateOrder() throws Exception {
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    @DisplayName("✅ 주문 조회 API 테스트 (ID)")
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    @DisplayName("✅ 주문 업데이트 API 테스트")
    void testUpdateOrder() throws Exception {
        Order updatedOrder = new Order();
        updatedOrder.setOid(1);
        updatedOrder.setCustomerName("Jane Doe");

        when(orderService.updateOrder(eq(1L), any(Order.class))).thenReturn(updatedOrder);

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Jane Doe"));
    }

    @Test
    @DisplayName("✅ 주문 삭제 API 테스트")
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(1L); // ✅ Stubbing이 필요하다면 doNothing() 사용

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk());

        verify(orderService).deleteOrder(1L);
    }
}

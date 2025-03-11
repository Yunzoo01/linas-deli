package com.linasdeli.api.controller.staff;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.dto.response.OrderResponseDTO;
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
@DisplayName("🧪 REST API - Staff Order Controller")
public class StaffOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequestDTO orderRequestDTO;
    private OrderResponseDTO orderResponseDTO;

    @BeforeEach
    void setUp() {
        orderRequestDTO = new OrderRequestDTO("John Doe","john.doe@example.com");

        orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOid(1L);
        orderResponseDTO.setCustomerName("John Doe");
        orderResponseDTO.setEmail("john.doe@example.com");
        orderResponseDTO.setPlatter("Deluxe Platter");
    }

    @Test
    @DisplayName("✅ 주문 생성 API 테스트")
    void testCreateOrder() throws Exception {
        when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(orderResponseDTO);

        mockMvc.perform(post("/api/staff/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.platter").value("Deluxe Platter"));
    }

    @Test
    @DisplayName("✅ 주문 조회 API 테스트 (ID)")
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(orderResponseDTO));

        mockMvc.perform(get("/api/staff/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.platter").value("Deluxe Platter"));
    }

    @Test
    @DisplayName("✅ 주문 업데이트 API 테스트")
    void testUpdateOrder() throws Exception {
        OrderRequestDTO updatedOrderRequestDTO = new OrderRequestDTO("John Doe","john.doe@example.com");
        updatedOrderRequestDTO.setCustomerName("Jane Doe");

        OrderResponseDTO updatedOrderResponseDTO = new OrderResponseDTO();
        updatedOrderResponseDTO.setOid(1L);
        updatedOrderResponseDTO.setCustomerName("Jane Doe");
        updatedOrderResponseDTO.setEmail("jane.doe@example.com");
        updatedOrderResponseDTO.setPlatter("Premium Platter");

        when(orderService.updateOrder(eq(1L), any(OrderRequestDTO.class))).thenReturn(updatedOrderResponseDTO);

        mockMvc.perform(put("/api/staff/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrderRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$.platter").value("Premium Platter"));
    }

    @Test
    @DisplayName("✅ 주문 삭제 API 테스트")
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/staff/orders/1"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).deleteOrder(1L);
    }
}

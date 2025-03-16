package com.linasdeli.api.dto.response;

import com.linasdeli.api.dto.OrderStatusCountDTO;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
public class OrderResponseDTO {
    private Page<com.linasdeli.api.dto.response.OrderDTO> orderList;
    private List<OrderStatusCountDTO> statusCounts;

    public OrderResponseDTO(Page<com.linasdeli.api.dto.response.OrderDTO> orderList, List<OrderStatusCountDTO> statusCounts) {
        this.orderList = orderList;
        this.statusCounts = statusCounts;
    }

    public Page<com.linasdeli.api.dto.response.OrderDTO> getOrderList() { return orderList; }
    public List<OrderStatusCountDTO> getStatusCounts() { return statusCounts; }
}

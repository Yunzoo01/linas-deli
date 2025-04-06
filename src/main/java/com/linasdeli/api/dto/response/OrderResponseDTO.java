package com.linasdeli.api.dto.response;

import com.linasdeli.api.dto.OrderDTO;
import com.linasdeli.api.dto.OrderStatusCountDTO;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
public class OrderResponseDTO {
    private Page<OrderDTO> orderList;
    private List<OrderStatusCountDTO> statusCounts;

    public OrderResponseDTO(Page<OrderDTO> orderList, List<OrderStatusCountDTO> statusCounts) {
        this.orderList = orderList;
        this.statusCounts = statusCounts;
    }

    public Page<OrderDTO> getOrderList() { return orderList; }
    public List<OrderStatusCountDTO> getStatusCounts() { return statusCounts; }
}

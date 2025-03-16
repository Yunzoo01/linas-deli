package com.linasdeli.api.dto;

public class OrderStatusCountDTO {
    private String status;
    private Long count;

    public OrderStatusCountDTO(String status, Long count) {
        this.status = status;
        this.count = count;
    }

    public String getStatus() { return status; }
    public Long getCount() { return count; }
}

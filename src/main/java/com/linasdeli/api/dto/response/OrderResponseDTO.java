package com.linasdeli.api.dto.response;

import com.linasdeli.api.domain.Order;
import lombok.Getter;

@Getter
public class OrderResponseDTO {
    private Long oid;
    private String platterName;
    private String customerName;
    private String email;
    private String status;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(Order order) {
        this.oid = order.getOid();
        this.platterName = (order.getPlatter() != null) ? order.getPlatter().getPlatterName() : "No Platter";
        this.customerName = order.getCustomerName();
        this.email = order.getEmail();
        this.status = order.getStatus();
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public void setPlatterName(String platter) {
        this.platterName = platter;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


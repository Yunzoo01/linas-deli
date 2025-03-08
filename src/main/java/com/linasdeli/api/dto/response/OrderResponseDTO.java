package com.linasdeli.api.dto.response;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.domain.Platter;
import lombok.Getter;

@Getter
public class OrderResponseDTO {
    private Long oid;
    private String platter;
    private String customerName;
    private String email;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(Order order) {
        this.oid = order.getOid();
        this.platter = order.getPlatter().getPlatterName();
        this.customerName = order.getCustomerName();
        this.email = order.getEmail();
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public void setPlatter(String platter) {
        this.platter = platter;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

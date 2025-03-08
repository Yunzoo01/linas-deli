package com.linasdeli.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {
    private String customerName;
    private String email;

    public OrderRequestDTO(String customerName, String email) {
        this.customerName = customerName;
        this.email = email;
    }
}

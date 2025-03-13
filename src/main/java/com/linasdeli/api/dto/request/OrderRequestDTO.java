package com.linasdeli.api.dto.request;

import com.linasdeli.api.domain.Platter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {
    private String customerName;
    private String email;
    private String platter;

    public OrderRequestDTO(String customerName, String email, String platter) {
        this.customerName = customerName;
        this.email = email;
        this.platter = platter;
    }
}

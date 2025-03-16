package com.linasdeli.api.dto.request;

import com.linasdeli.api.domain.Platter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderRequestDTO {
    private String platter;
    private String customerName;
    private String email;
    private String phone;
    private String status;
    private LocalDate date;
    private String time;
    private String allergy;
    private String message;

    public OrderRequestDTO(String customerName, String email, String platter, String phone, String status, LocalDate date, String time, String allergy, String message) {
        this.customerName = customerName;
        this.email = email;
        this.platter = platter;
        this.phone = phone;
        this.status = status;
        this.date = date;
        this.time = time;
        this.allergy = allergy;
        this.message = message;
    }
}

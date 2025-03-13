package com.linasdeli.api.dto.response;

import com.linasdeli.api.domain.Order;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public class OrderResponseDTO {
    private Long oid;
    private String platterName;
    private String customerName;
    private String email;
    private String phone;
    private String status;
    private LocalDate date;
    private String time;
    private String allergy;
    private String message;


    public OrderResponseDTO() {
    }

    public OrderResponseDTO(Order order) {
        this.oid = order.getOid();
        this.platterName = (order.getPlatter() != null) ? order.getPlatter().getPlatterName() : "No Platter";
        this.customerName = order.getCustomerName();
        this.phone = order.getPhone();
        this.email = order.getEmail();
        this.status = order.getStatus();
        this.date = order.getDate().toLocalDate();
        this.time = order.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.allergy = order.getAllergy();
        this.message = order.getMessage();
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(LocalDateTime date) {
        this.date = date.toLocalDate();
    }

    public void setTime(LocalTime time) {
        this.time = time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }
}


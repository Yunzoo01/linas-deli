package com.linasdeli.api.dto;

import com.linasdeli.api.domain.Order;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public class OrderDTO {
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
    private LocalDateTime createdAt; // 주문 생성 시간 필드 추가

    public OrderDTO() {
    }

    public OrderDTO(Order order) {
        this.oid = order.getOid();
        this.platterName = (order.getPlatter() != null) ? order.getPlatter().getPlatterName() : "No Platter";
        this.customerName = order.getCustomerName();
        this.phone = order.getPhone();
        this.email = order.getEmail();
        this.status = order.getStatus();
        this.date = order.getDate();
        this.time = order.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.allergy = order.getAllergy();
        this.message = order.getMessage();
        this.createdAt = order.getCreatedAt(); // 생성자에서 createdAt 정보 복사
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
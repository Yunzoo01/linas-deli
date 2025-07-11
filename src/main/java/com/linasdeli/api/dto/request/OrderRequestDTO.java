package com.linasdeli.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class OrderRequestDTO {
    @NotBlank(message = "Platter is required")
    private String platterName;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    private String phone;

    private String status;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Time is required")
    private LocalTime time;

    private long amount;

    // 알러지와 메시지는 필수 항목이 아니라면 어노테이션 없이 두거나, 필요에 따라 @Size 등을 적용할 수 있습니다.
    private String allergy;
    private String message;

    private String transactionId;
    private String paymentStatus;

    public OrderRequestDTO() {
        // 기본 생성자
    }

    public OrderRequestDTO(String customerName, String email, String platterName, String phone, String status, LocalDate date, LocalTime time, String allergy, String message, Long amount, String transactionId, String paymentStatus) {
        this.customerName = customerName;
        this.email = email;
        this.platterName = platterName;
        this.phone = phone;
        this.status = status;
        this.date = date;
        this.time = time;
        this.allergy = allergy;
        this.message = message;
        this.amount = amount;
        this.transactionId = "transactionId";
        this.paymentStatus = "paymentStatus";
    }
}

package com.linasdeli.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Order entity - Stores customer order history
 */
@Entity
@Table(name = "platter_order") // "order" is a reserved SQL keyword, renamed to "order_table"
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    @ManyToOne
    @JoinColumn(name = "platter_id", nullable = false)
    private Platter platter;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(length = 100, nullable = false)
    private String customerName;

    @Column(length = 20)
    private String phone;

    @Column(length = 500)
    private String allergy;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 1000)
    private String message;

    @Column(length = 20)
    private String status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

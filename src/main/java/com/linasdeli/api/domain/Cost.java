package com.linasdeli.api.domain;

import com.linasdeli.api.domain.enums.PriceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Cost entity - Stores product pricing information
 */
@Getter
@Setter
@Entity
@Table(name = "cost")
public class Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer costId;

    @ManyToOne
    @JoinColumn(name = "pid", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PriceType priceType;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal supplierPrice;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal divisor;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal retailPrice;
    private Integer plu; // Code for weight-pricetype items
}
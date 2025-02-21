package com.linasdeli.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
/**
 * ProductDetail entity - Stores detailed product information (applicable to specific products such as cheese, ham)
 */
@Getter
@Setter
@Entity
@Table(name = "product_detail")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productDetailId;

    @ManyToOne
    @JoinColumn(name = "pid", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
}
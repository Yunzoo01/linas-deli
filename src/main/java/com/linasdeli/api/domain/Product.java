package com.linasdeli.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Product entity - Stores product information - Others only use this table
 */
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;

    @Column(length = 100) private String imageName;
    @Column(length = 500) private String imageUrl;
    @Column(nullable = false) private String productName;
    private String allergy;
    private Boolean pasteurized;
    @Column(length = 100) private String ingredientsImageName;
    @Column(length = 500) private String ingredientsImageUrl;
    @Column(length = 1000) private String description;
    @Column(length = 1000) private String servingSuggestion;

    @ManyToOne
    @JoinColumn(name = "supplier", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDetail> productDetails;

    @Column(nullable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime updatedAt;
}
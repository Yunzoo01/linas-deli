package com.linasdeli.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "promotion")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;

    @Column(nullable = false, length = 200) private String promotionTitle;

    @Column(length = 100) private String promotionImageName;

    @Column(length = 500) private String promotionImageUrl;

    @Column(nullable = false) private LocalDateTime startDate;

    @Column(nullable = false) private LocalDateTime endDate;

    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;

    @Column(nullable = false) private LocalDateTime updatedAt;
}
package com.linasdeli.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
/**
 * User entity - Stores Staff authentication information
 */
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    @Column(nullable = false, length = 100)
    private String userName;

    @Column(nullable = false, unique = true, length = 100)
    private String id;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
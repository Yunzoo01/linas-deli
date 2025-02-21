package com.linasdeli.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "platter")
public class Platter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer platterId;

    @Column(nullable = false, length = 100) private String platterName;
}
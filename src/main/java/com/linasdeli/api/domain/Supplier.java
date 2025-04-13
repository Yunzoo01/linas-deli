package com.linasdeli.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sid;

    @Column(nullable = false, length = 200) private String supplierName;

    @Column(length = 100)
    private String contactName;

    @Column(length = 100)
    private String sEmail;

    @Column(length = 20)
    private String sPhone;

    @Column(length = 500)
    private String note;
}

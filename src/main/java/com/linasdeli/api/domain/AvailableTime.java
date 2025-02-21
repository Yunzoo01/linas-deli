package com.linasdeli.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@Entity
@Table(name = "available_time")
public class AvailableTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tid;
    /* Todo : change ERD data type string -> Time
        it's more easy to calculate time
    */
    private Time time;
}

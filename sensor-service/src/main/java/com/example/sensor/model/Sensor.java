package com.example.sensor.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String location;

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;
}
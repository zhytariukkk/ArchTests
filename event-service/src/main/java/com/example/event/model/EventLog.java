package com.example.event.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
}
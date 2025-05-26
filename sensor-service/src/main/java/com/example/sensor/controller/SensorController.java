package com.example.sensor.controller;

import com.example.sensor.model.Sensor;
import com.example.sensor.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
public class SensorController {
    private final SensorRepository sensorRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public List<Sensor> getAll() {
        return sensorRepository.findAll();
    }

    @PostMapping
    public Sensor create(@RequestBody Sensor sensor) {
        Sensor saved = sensorRepository.save(sensor);
        kafkaTemplate.send("sensor-events", "New sensor added: " + saved.getId());
        return saved;
    }
}
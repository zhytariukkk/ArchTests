package com.example.device.controller;

import com.example.device.model.Device;
import com.example.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceRepository deviceRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public Device create(@RequestBody Device device) {
        Device saved = deviceRepository.save(device);
        kafkaTemplate.send("device-events", "New device added: " + saved.getId());
        return saved;
    }
    @GetMapping
    public List<Device> getDevices(@RequestParam(required = false) Long roomId) {
        if (roomId != null) {
            return deviceRepository.findByRoomId(roomId);
        }
        return deviceRepository.findAll();
    }
}
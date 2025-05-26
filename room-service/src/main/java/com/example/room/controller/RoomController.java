package com.example.room.controller;
import com.example.room.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import com.example.room.model.Room;
import com.example.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomRepository roomRepository;
    private final RestTemplate restTemplate;

    @GetMapping("/{id}/devices")
    public List<DeviceDto> getDevices(@PathVariable Long id) {
        roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
        String url = "http://device-service/devices?roomId=" + id;
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DeviceDto>>() {}
        ).getBody();
    }
    @GetMapping
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @PostMapping
    public Room create(@RequestBody Room room) {
        return roomRepository.save(room);
    }
}
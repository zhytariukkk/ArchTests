package com.example.room.dto;

public record DeviceDto(
        Long id,
        String name,
        String type,
        Long roomId
) {}

package com.example.event.listener;

import com.example.event.model.EventLog;
import com.example.event.repository.EventLogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SensorEventListener {

    private final EventLogRepository repository;

    public SensorEventListener(EventLogRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "sensor-events", groupId = "event-group")
    public void consume(String message) {
        EventLog log = new EventLog();
        log.setMessage(message);
        repository.save(log);
    }
}
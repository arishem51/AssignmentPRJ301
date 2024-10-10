package com.example.scheduling_system.config;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.example.scheduling_system.models.Shift;
import com.example.scheduling_system.repositories.ShiftRepository;

import jakarta.annotation.PostConstruct;

@Component
public class ShiftInitializer {
    private final ShiftRepository shiftRepository;

    public ShiftInitializer(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("Count" + shiftRepository.count());
        if (shiftRepository.count() == 0) {
            shiftRepository.save(new Shift("S1", LocalTime.of(0, 0), LocalTime.of(8, 0)));
            shiftRepository.save(new Shift("S2", LocalTime.of(8, 0), LocalTime.of(16, 0)));
            shiftRepository.save(new Shift("S3", LocalTime.of(16, 0), LocalTime.of(0, 0)));
        }
    }
}

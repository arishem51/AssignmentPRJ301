package com.example.scheduling_system.services;

import org.springframework.stereotype.Service;
import com.example.scheduling_system.models.Shift;
import com.example.scheduling_system.repositories.ShiftRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;

    public Shift findById(Long id) {
        return shiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));
    }

}

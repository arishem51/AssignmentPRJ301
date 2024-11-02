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

    public Shift firstShift() {
        return this.findById(1L);
    }

    public Shift secondShift() {
        return this.findById(2L);
    }

    public Shift thirdShift() {
        return this.findById(3L);
    }

    public Shift getShiftByIndex(int index) {
        if (index == 1) {
            return this.firstShift();
        }
        if (index == 2) {
            return this.secondShift();
        }
        if (index == 3) {
            return this.thirdShift();
        }
        return this.thirdShift();
    }

}

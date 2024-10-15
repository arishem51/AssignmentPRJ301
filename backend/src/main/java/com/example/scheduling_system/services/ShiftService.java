package com.example.scheduling_system.services;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.models.Shift;
import com.example.scheduling_system.repositories.ShiftRepository;

@Service
public class ShiftService {
    @Autowired
    ShiftRepository shiftRepository;

    public Shift findById(Long id) {
        return shiftRepository.findById(id).orElseThrow(() -> new RuntimeErrorException(new Error("Shift not found!")));
    }

}

package com.example.scheduling_system.services;

import org.springframework.stereotype.Service;

import com.example.scheduling_system.models.WorkerScheduleCampaign;
import com.example.scheduling_system.repositories.WorkerScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkerScheduleService {
    private final WorkerScheduleRepository workerScheduleRepository;

    public void save(WorkerScheduleCampaign item) {
        workerScheduleRepository.save(item);
    }
}

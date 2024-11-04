package com.example.scheduling_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.scheduling_system.models.WorkerScheduleCampaign;

public interface WorkerScheduleRepository extends JpaRepository<WorkerScheduleCampaign, Long> {

}

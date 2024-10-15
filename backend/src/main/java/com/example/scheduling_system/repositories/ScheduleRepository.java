package com.example.scheduling_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduling_system.models.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}

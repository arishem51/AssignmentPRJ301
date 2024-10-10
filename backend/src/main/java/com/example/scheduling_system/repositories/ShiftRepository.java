package com.example.scheduling_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduling_system.models.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

}

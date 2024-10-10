package com.example.scheduling_system.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduling_system.models.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Page<Plan> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

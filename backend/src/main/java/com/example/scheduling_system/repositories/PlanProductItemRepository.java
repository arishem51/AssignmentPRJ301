package com.example.scheduling_system.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduling_system.models.PlanProductItem;

public interface PlanProductItemRepository extends JpaRepository<PlanProductItem, Long> {
    Page<PlanProductItem> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

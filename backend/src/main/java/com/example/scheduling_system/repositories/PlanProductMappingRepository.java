package com.example.scheduling_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.scheduling_system.models.PlanProductMapping;

@Repository
public interface PlanProductMappingRepository extends JpaRepository<PlanProductMapping, Long> {

}

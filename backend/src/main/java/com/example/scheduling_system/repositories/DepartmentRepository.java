package com.example.scheduling_system.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduling_system.models.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Page<Department> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

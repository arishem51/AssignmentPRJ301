package com.example.scheduling_system.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final String name;
    private final double hourlyWage;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private final Department department;

    @Enumerated(EnumType.STRING)
    private final Role role;

    public enum Role {
        MANAGER,
        WORKER,
    }

}

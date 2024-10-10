package com.example.scheduling_system.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double workingRate = 1;

    private double hourlyWage;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        MANAGER,
        WORKER
    }

    public Employee() {
    }

    public Employee(String name, double hourlyWage, Role role) {
        this.name = name;
        this.hourlyWage = hourlyWage;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWorkingRate() {
        return workingRate;
    }

    public void setWorkingRate(double workingRate) {
        this.workingRate = workingRate;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}

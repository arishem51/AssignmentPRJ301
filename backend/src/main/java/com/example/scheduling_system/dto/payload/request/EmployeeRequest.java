package com.example.scheduling_system.dto.payload.request;

import com.example.scheduling_system.models.Employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeRequest(@NotBlank String name, @NotNull double hourlyWage,
        Employee.Role role, @NotNull Long departmentId) {
}

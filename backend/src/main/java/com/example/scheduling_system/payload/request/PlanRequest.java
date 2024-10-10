package com.example.scheduling_system.payload.request;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlanRequest(@NotBlank String name, @NotNull Date startDate, @NotNull Date endDate,
                Optional<Set<Long>> planProductIds) {
}

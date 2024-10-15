package com.example.scheduling_system.dto.payload.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record SchedulePlanRequest(@NotBlank Set<Long> s1, Set<Long> s2, Set<Long> s3) {

}

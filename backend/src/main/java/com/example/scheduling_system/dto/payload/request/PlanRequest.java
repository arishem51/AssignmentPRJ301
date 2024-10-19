package com.example.scheduling_system.dto.payload.request;

import java.util.Date;
import java.util.List;

import com.example.scheduling_system.dto.Campaign;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PlanRequest(@NotBlank String name, @NotNull Date startDate, @NotNull Date endDate,
                @NotEmpty List<Campaign> campaigns) {

}

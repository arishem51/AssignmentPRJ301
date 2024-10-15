package com.example.scheduling_system.dto.payload.request;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

public record ScheduleRequest(@NotNull Long planId, @NotNull Long shiftId, @NotNull Long empId, Date date,
        @NotNull Long prodId) {

}

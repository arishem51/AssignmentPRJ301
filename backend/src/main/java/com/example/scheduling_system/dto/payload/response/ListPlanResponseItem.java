package com.example.scheduling_system.dto.payload.response;

import java.util.Date;

import com.example.scheduling_system.models.Plan;

public record ListPlanResponseItem(Long id, Date startDate, Date endDate, String name, Plan.Status status) {
}

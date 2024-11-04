package com.example.scheduling_system.dto.payload.request;

import java.util.Set;
import com.example.scheduling_system.dto.WorkerShiftAllocation;
import jakarta.validation.constraints.NotBlank;

public record SchedulePlanRequest(@NotBlank long scheduleCampaignId,
        @NotBlank Set<WorkerShiftAllocation> workerAllocation,
        @NotBlank int shiftId) {

}

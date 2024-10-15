package com.example.scheduling_system.dto.payload.response;

import java.util.Date;
import com.example.scheduling_system.models.Plan;
import lombok.Getter;

public class ListPlanResponseItem {
    @Getter
    private Long id;
    @Getter
    private Date startDate;
    @Getter
    private Date endDate;
    @Getter
    private String name;
    @Getter
    private Plan.Status status;

    public ListPlanResponseItem(Plan plan) {
        this.id = plan.getId();
        this.startDate = plan.getStartDate();
        this.endDate = plan.getEndDate();
        this.name = plan.getName();
        this.status = plan.getStatus();
    }
}

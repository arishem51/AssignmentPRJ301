package com.example.scheduling_system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class WorkerScheduleCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Employee worker;

    @ManyToOne
    @JoinColumn(name = "schedule_campaign_id", nullable = false)
    @JsonIgnore
    private ScheduleCampaign scheduleCampaign;

    @NotNull
    private Integer quantity;

    // Constructor with fields
    public WorkerScheduleCampaign(Employee worker, ScheduleCampaign scheduleCampaign, Integer quantity) {
        this.worker = worker;
        this.scheduleCampaign = scheduleCampaign;
        this.quantity = quantity;
    }
}

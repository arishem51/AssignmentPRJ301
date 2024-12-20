package com.example.scheduling_system.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "ScheduleCampaign")
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ScheduleCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_campaign_id")
    @JsonIgnore
    @NotNull
    private final PlanCampaign planCampaign;

    @NotNull
    private final int quantity;

    @NotNull
    private final Date date;

    @NotNull
    private final Date endDate;

    @ManyToMany
    @JoinTable(name = "schedule_campaign_shift", // Name of the join table
            joinColumns = @JoinColumn(name = "schedule_campaign_id"), // Foreign key for ScheduleCampaign
            inverseJoinColumns = @JoinColumn(name = "shift_id") // Foreign key for Shift
    )
    @NotNull
    private final List<Shift> shifts;

    @OneToMany(mappedBy = "scheduleCampaign")
    private List<WorkerScheduleCampaign> workerScheduleCampaigns;
}

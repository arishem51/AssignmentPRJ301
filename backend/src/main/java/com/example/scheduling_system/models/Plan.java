package com.example.scheduling_system.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "plans")
@Data
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotBlank
    private String name;

    public enum Status {
        OPEN,
        DONE
    }

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanCampaign> planCampaigns;

    public Plan() {
        this.status = Status.OPEN;
    }

    public Plan(String name, Date startDate, Date endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = Status.OPEN;
    }

}

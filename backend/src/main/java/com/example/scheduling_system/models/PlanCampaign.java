package com.example.scheduling_system.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PlanCampaign")
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class PlanCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull
    private final Product product;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    @NotNull
    @JsonIgnore
    private final Plan plan;

    @NotNull
    private final double quantity;

    @NotNull
    private final double estimateEffort;

    @OneToMany(mappedBy = "planCampaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleCampaign> scheduleCampaigns;
}

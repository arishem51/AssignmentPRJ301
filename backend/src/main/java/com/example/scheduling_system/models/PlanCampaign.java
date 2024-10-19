package com.example.scheduling_system.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PlanCampaign")
@Getter
@Setter
@RequiredArgsConstructor
public class PlanCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    @NotNull
    private final Product product;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    @NotNull
    private final Plan plan;

    @NotNull
    private final double quantity;
    @NotNull
    private final double estimateEffort;
}

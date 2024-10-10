package com.example.scheduling_system.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @ManyToMany
    @JoinTable(name = "plan_product_mappings", joinColumns = @JoinColumn(name = "plan_id"), inverseJoinColumns = @JoinColumn(name = "plan_product_item_id"))
    private List<PlanProductItem> planProducts;

    public Plan() {
    }

    public Plan(String name, Date startDate, Date endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Plan(String name, Date startDate, Date endDate, List<PlanProductItem> planProducts) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planProducts = planProducts;
    }

    @NotBlank
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<PlanProductItem> getPlanProducts() {
        return planProducts;
    }

    public void setPlanProducts(List<PlanProductItem> planProducts) {
        this.planProducts = planProducts;
    }

}

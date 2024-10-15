package com.example.scheduling_system.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "plan_product_mappings")
public class PlanProductMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    public PlanProductMapping() {
    }

    public PlanProductMapping(Plan plan, PlanProductItem planProductItem, int quantity) {
        this.plan = plan;
        this.planProductItem = planProductItem;
        this.quantity = quantity;
    }

    @ManyToOne
    @JoinColumn(name = "plan_product_item_id")
    private PlanProductItem planProductItem;

    private int quantity;

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public PlanProductItem getPlanProductItem() {
        return planProductItem;
    }

    public void setPlanProductItem(PlanProductItem planProductItem) {
        this.planProductItem = planProductItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

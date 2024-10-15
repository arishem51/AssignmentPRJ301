package com.example.scheduling_system.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "plan_product_items")
public class PlanProductItem implements Cloneable {

    @NotBlank
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    public PlanProductItem() {
    }

    public PlanProductItem(String name, Product product, @NotNull double quantity) {
        this.product = product;
        this.quantity = quantity;
        this.name = name;
    }

    @Override
    public PlanProductItem clone() {
        try {
            return (PlanProductItem) super.clone();
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    @NotNull
    private double quantity;

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}

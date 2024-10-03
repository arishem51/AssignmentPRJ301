package com.example.scheduling_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "estimated_effort")
    private Double estimatedEffort;

    @Column(name = "img")
    private String img;

    public Product() {
    }

    public Product(String name, Double estimatedEffort, String img) {
        this.name = name;
        this.estimatedEffort = estimatedEffort;
        this.img = img;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getEstimatedEffort() {
        return estimatedEffort;
    }

    public void setEstimatedEffort(Double estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

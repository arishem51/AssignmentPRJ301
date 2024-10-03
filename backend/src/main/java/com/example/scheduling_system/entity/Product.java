package com.example.scheduling_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products") // This specifies the name of the table in the database
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates Product ID
    private Long id; // Product ID

    @Column(name = "product_name", nullable = false) // Product Name column
    private String name;

    @Column(name = "estimated_effort") // Estimated Effort column (per hour)
    private Double estimatedEffort; // Use Double or Integer depending on your needs

    @Column(name = "img") // Column for image
    private String img; // Path or URL to the image file

    // Constructors, Getters, and Setters

    public Product() {
    }

    public Product(String name, Double estimatedEffort, String img) {
        this.name = name;
        this.estimatedEffort = estimatedEffort;
        this.img = img;
    }

    // Getters and Setters
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

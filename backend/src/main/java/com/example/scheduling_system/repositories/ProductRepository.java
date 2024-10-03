package com.example.scheduling_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduling_system.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

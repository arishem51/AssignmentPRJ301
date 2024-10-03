package com.example.scheduling_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduling_system.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

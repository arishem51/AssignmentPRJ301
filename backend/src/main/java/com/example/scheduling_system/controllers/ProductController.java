package com.example.scheduling_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduling_system.payload.request.ProductRequest;
import com.example.scheduling_system.payload.response.BodyResponse;
import com.example.scheduling_system.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String search) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize);
            var products = productService.findAll(pageable, search);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", products).getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Get product error!").getBodyResponse());
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        try {
            var product = productService.create(request);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", product).getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Create product error!").getBodyResponse());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody ProductRequest request) {
        try {
            var product = productService.update(Long.parseLong(id), request);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", product).getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Update product error!").getBodyResponse());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        try {
            productService.delete(Long.parseLong(id));
            return ResponseEntity.ok().body(new BodyResponse<>("Success").getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Delete product error!").getBodyResponse());
        }
    }
}

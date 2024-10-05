package com.example.scheduling_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.dto.Meta;
import com.example.scheduling_system.models.Product;
import com.example.scheduling_system.payload.request.ProductRequest;
import com.example.scheduling_system.payload.response.PaginateResponse;
import com.example.scheduling_system.payload.response.ProductResponse;
import com.example.scheduling_system.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    private ProductResponse mapProductToProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getEstimatedEffort(), product.getImg());
    }

    public PaginateResponse<ProductResponse> findAll(Pageable pageable) {
        var productPage = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = productPage.getContent().stream().map(
                this::mapProductToProductResponse)
                .collect(Collectors.toList());
        Meta meta = new Meta(productPage.getNumber() + 1, productPage.getSize(), productPage.getTotalElements(),
                productPage.getTotalPages());
        return new PaginateResponse<>(productResponses, meta);
    }

    public ProductResponse create(ProductRequest request) {
        var product = new Product(request.name(), request.estimatedEffort(), request.img());
        productRepository.save(product);
        return mapProductToProductResponse(product);
    }
}

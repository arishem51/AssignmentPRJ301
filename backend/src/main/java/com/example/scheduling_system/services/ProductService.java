package com.example.scheduling_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.dto.Meta;
import com.example.scheduling_system.dto.payload.request.ProductRequest;
import com.example.scheduling_system.dto.payload.response.PaginateResponse;
import com.example.scheduling_system.dto.payload.response.ProductResponse;
import com.example.scheduling_system.models.Product;
import com.example.scheduling_system.repositories.ProductRepository;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    private ProductResponse mapProductToProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getEstimatedEffort(), product.getImg());
    }

    public PaginateResponse<ProductResponse> findAll(Pageable pageable, String search) {
        var productPage = search.isEmpty() ? productRepository.findAll(pageable)
                : productRepository.findByNameContainingIgnoreCase(search, pageable);
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

    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeJsonMappingException("Not found!"));
    }

    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    public ProductResponse update(long id, ProductRequest request) {
        var product = this.findById(id);
        product.setName(request.name());
        product.setImg(request.img());
        product.setEstimatedEffort(request.estimatedEffort());
        productRepository.save(product);
        return mapProductToProductResponse(product);
    }

    public void delete(long id) {
        var product = this.findById(id);
        productRepository.delete(product);
    }

}

package com.example.scheduling_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.models.PlanProductItem;
import com.example.scheduling_system.payload.request.PlanProductItemRequest;
import com.example.scheduling_system.repositories.PlanProductItemRepository;

@Service
public class PlanProductItemService {
    @Autowired
    private PlanProductItemRepository planProductItemRepository;

    @Autowired
    private ProductService productService;

    public PlanProductItem create(PlanProductItemRequest request) {
        try {
            var product = this.productService.findById(request.productId());
            PlanProductItem planProductItem = new PlanProductItem(request.name(), product, request.quantity());
            return planProductItemRepository.save(planProductItem);
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }

}

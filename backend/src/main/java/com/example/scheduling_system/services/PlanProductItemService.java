package com.example.scheduling_system.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.dto.Meta;
import com.example.scheduling_system.dto.payload.request.PlanProductItemRequest;
import com.example.scheduling_system.dto.payload.response.PaginateResponse;
import com.example.scheduling_system.models.PlanProductItem;
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

    public PaginateResponse<PlanProductItem> findAll(Pageable pageable, String search) {
        var planProductItemPage = search.isEmpty() ? planProductItemRepository.findAll(pageable)
                : planProductItemRepository.findByNameContainingIgnoreCase(search, pageable);
        List<PlanProductItem> planProductResponse = planProductItemPage.getContent().stream()
                .collect(Collectors.toList());
        Meta meta = new Meta(planProductItemPage.getNumber() + 1, planProductItemPage.getSize(),
                planProductItemPage.getTotalElements(),
                planProductItemPage.getTotalPages());
        return new PaginateResponse<>(planProductResponse, meta);
    }

    public List<PlanProductItem> findAllById(Set<Long> ids) {
        return planProductItemRepository.findAllById(ids);
    }

}

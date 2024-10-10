package com.example.scheduling_system.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.dto.Meta;
import com.example.scheduling_system.models.Plan;
import com.example.scheduling_system.payload.request.PlanRequest;
import com.example.scheduling_system.payload.response.PaginateResponse;
import com.example.scheduling_system.repositories.PlanRepository;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanProductItemService planProductItemService;

    public Plan create(PlanRequest request) {
        var plan = new Plan(request.name(), request.startDate(), request.endDate());
        var planProducts = planProductItemService.findAllById(request.planProductIds().get());
        if (planProducts.size() > 0) {
            plan.setPlanProducts(planProducts);
        }
        return planRepository.save(plan);
    }

    public PaginateResponse<Plan> findAll(Pageable pageable, String search) {
        var planPage = search.isEmpty() ? planRepository.findAll(pageable)
                : planRepository.findByNameContainingIgnoreCase(search, pageable);
        List<Plan> planResponse = planPage.getContent().stream().collect(Collectors.toList());
        Meta meta = new Meta(planPage.getNumber() + 1, planPage.getSize(), planPage.getTotalElements(),
                planPage.getTotalPages());
        return new PaginateResponse<>(planResponse, meta);
    }
}

package com.example.scheduling_system.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.dto.Meta;
import com.example.scheduling_system.dto.payload.request.PlanRequest;
import com.example.scheduling_system.dto.payload.response.ListPlanResponseItem;
import com.example.scheduling_system.dto.payload.response.PaginateResponse;
import com.example.scheduling_system.models.Plan;
import com.example.scheduling_system.repositories.PlanRepository;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

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

    public Plan update(Long id, PlanRequest request) {
        var plan = planRepository.findById(id).get();
        var planProducts = planProductItemService.findAllById(request.planProductIds().get());
        if (planProducts.size() > 0) {
            plan.setPlanProducts(planProducts);
        }
        plan.setEndDate(request.endDate());
        plan.setStartDate(request.startDate());
        return planRepository.save(plan);
    }

    private ListPlanResponseItem mapPlanToPlanResponseItem(Plan plan) {
        return new ListPlanResponseItem(plan.getId(), plan.getStartDate(), plan.getEndDate(), plan.getName(),
                plan.getStatus());
    }

    public PaginateResponse<ListPlanResponseItem> findAll(Pageable pageable, String search) {
        var planPage = search.isEmpty() ? planRepository.findAll(pageable)
                : planRepository.findByNameContainingIgnoreCase(search, pageable);
        List<ListPlanResponseItem> planResponse = planPage.getContent().stream().map(this::mapPlanToPlanResponseItem)
                .collect(Collectors.toList());
        Meta meta = new Meta(planPage.getNumber() + 1, planPage.getSize(), planPage.getTotalElements(),
                planPage.getTotalPages());
        return new PaginateResponse<>(planResponse, meta);
    }

    public Plan findById(Long id) {
        return planRepository.findById(id).orElseThrow(() -> new RuntimeJsonMappingException("Plan not found!"));
    }
}

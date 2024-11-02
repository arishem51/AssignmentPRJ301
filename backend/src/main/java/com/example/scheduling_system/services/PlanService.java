package com.example.scheduling_system.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.scheduling_system.dto.Campaign;
import com.example.scheduling_system.dto.Meta;
import com.example.scheduling_system.dto.payload.request.PlanRequest;
import com.example.scheduling_system.dto.payload.response.ListPlanResponseItem;
import com.example.scheduling_system.dto.payload.response.PaginateResponse;
import com.example.scheduling_system.models.Plan;
import com.example.scheduling_system.models.PlanCampaign;
import com.example.scheduling_system.models.Product;
import com.example.scheduling_system.models.ScheduleCampaign;
import com.example.scheduling_system.repositories.PlanRepository;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final ProductService productService;
    private final ScheduleCampaignService scheduleCampaignService;

    private Map<Long, Product> getProductFromRequest(PlanRequest request) {
        List<Long> productIds = request.campaigns().stream().map(Campaign::productId).collect(Collectors.toList());
        return productService.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    @Transactional
    public Plan create(PlanRequest request) {
        Plan plan = new Plan(request.name(), request.startDate(), request.endDate());
        List<PlanCampaign> planCampaigns = new ArrayList<>();
        Map<Long, Product> products = getProductFromRequest(request);

        for (Campaign campaign : request.campaigns()) {
            Product product = products.get(campaign.productId());
            PlanCampaign planCampaign = new PlanCampaign(product, plan, campaign.quantity(), campaign.estimateEffort());

            List<ScheduleCampaign> scheduleCampaigns = scheduleCampaignService.schedulingCampaign(planCampaign);
            planCampaign.setScheduleCampaigns(scheduleCampaigns);
            planCampaigns.add(planCampaign);
        }
        plan.setPlanCampaigns(planCampaigns);
        return planRepository.save(plan);
    }

    public Plan update(Long id, PlanRequest request) {
        Plan plan = planRepository.findById(id).get();
        // plan.setEndDate(request.endDate());
        // plan.setStartDate(request.startDate());
        // return planRepository.save(plan);
        return plan;
    }

    private ListPlanResponseItem mapPlanToPlanResponseItem(Plan plan) {
        return new ListPlanResponseItem(plan);
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

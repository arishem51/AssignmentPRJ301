package com.example.scheduling_system.services;

import org.springframework.stereotype.Service;
import com.example.scheduling_system.models.PlanProductMapping;
import com.example.scheduling_system.repositories.PlanProductMappingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanProductMappingService {
    private final PlanProductMappingRepository planProductMappingRepository;

    public PlanProductMapping findById(Long id) {
        return planProductMappingRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));
    }

    public void update(PlanProductMapping item) {
        planProductMappingRepository.save(item);
    }

    public List<PlanProductMapping> getByPlanId(Long planId) {
        return planProductMappingRepository.findByPlan_Id(planId);
    }

}

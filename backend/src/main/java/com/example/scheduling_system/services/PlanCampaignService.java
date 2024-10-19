package com.example.scheduling_system.services;

import org.springframework.stereotype.Service;
import com.example.scheduling_system.repositories.PlanCampaignRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanCampaignService {
    private final PlanCampaignRepository planCampaignRepository;
}

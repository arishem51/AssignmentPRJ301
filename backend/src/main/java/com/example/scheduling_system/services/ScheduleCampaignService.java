package com.example.scheduling_system.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.scheduling_system.models.PlanCampaign;
import com.example.scheduling_system.models.ScheduleCampaign;
import lombok.RequiredArgsConstructor;

@Service
public class ScheduleCampaignService {
    public List<ScheduleCampaign> schedulingCampaign(PlanCampaign planCampaign) {
        List<ScheduleCampaign> scheduleCampaigns = new ArrayList<>();
        return scheduleCampaigns;
    }
}

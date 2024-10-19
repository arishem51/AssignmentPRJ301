package com.example.scheduling_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduling_system.models.PlanCampaign;

public interface PlanCampaignRepository extends JpaRepository<PlanCampaign, Long> {

}

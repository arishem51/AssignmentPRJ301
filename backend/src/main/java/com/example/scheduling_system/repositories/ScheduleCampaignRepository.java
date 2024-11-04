package com.example.scheduling_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scheduling_system.models.ScheduleCampaign;

public interface ScheduleCampaignRepository extends JpaRepository<ScheduleCampaign, Long> {

}

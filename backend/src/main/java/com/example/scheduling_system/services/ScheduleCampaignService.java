package com.example.scheduling_system.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import com.example.scheduling_system.models.PlanCampaign;
import com.example.scheduling_system.models.ScheduleCampaign;
import com.example.scheduling_system.models.Shift;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleCampaignService {
    private final int SHIFT_PER_DAY = 3;
    private final ShiftService shiftService;

    private ScheduleCampaign createScheduleCampaign(PlanCampaign planCampaign, int quantity, int shiftIndex,
            Date startDate, Calendar calendar) {
        int dayOffset = shiftIndex / SHIFT_PER_DAY;
        int dailyShiftIndex = (shiftIndex + 1) % SHIFT_PER_DAY;

        Shift shift = shiftService.getShiftByIndex(dailyShiftIndex);
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, dayOffset);
        Date schDate = calendar.getTime();

        return new ScheduleCampaign(planCampaign, quantity, schDate, shift);
    }

    public List<ScheduleCampaign> schedulingCampaign(PlanCampaign planCampaign) {
        List<ScheduleCampaign> scheduleCampaigns = new ArrayList<>();

        Date startDate = planCampaign.getPlan().getStartDate();
        Date endDate = planCampaign.getPlan().getEndDate();
        Calendar calendar = Calendar.getInstance();

        long diffInMillis = endDate.getTime() - startDate.getTime();
        int totalDays = ((int) TimeUnit.MILLISECONDS.toDays(diffInMillis)) + 1;
        int totalProducts = (int) planCampaign.getQuantity();
        int totalShifts = totalDays * SHIFT_PER_DAY;
        int baseProductPerShift = totalProducts / totalShifts;
        int remainingProducts = totalProducts % totalShifts;
        int[] campaigns = new int[totalShifts];

        if (totalProducts < totalShifts) {
            for (int i = 0; i < totalProducts; i++) {
                scheduleCampaigns.add(createScheduleCampaign(planCampaign, 1, i, startDate, calendar));
            }
            return scheduleCampaigns;
        }

        for (int i = 0; i < totalShifts; i++) {
            campaigns[i] = baseProductPerShift;
            if (remainingProducts > 0) {
                campaigns[i] += 1;
                remainingProducts--;
            }
            scheduleCampaigns.add(createScheduleCampaign(planCampaign, campaigns[i], i, startDate, calendar));
        }

        return scheduleCampaigns;
    }
}

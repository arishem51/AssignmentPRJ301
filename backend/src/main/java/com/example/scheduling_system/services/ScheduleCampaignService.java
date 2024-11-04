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
import com.example.scheduling_system.repositories.ScheduleCampaignRepository;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleCampaignService {
    private final int SHIFT_PER_DAY = 3;
    private final ShiftService shiftService;
    private final ScheduleCampaignRepository schCampaignRepository;

    public ScheduleCampaign findById(long id) {
        return schCampaignRepository.findById(id).orElseThrow(() -> new RuntimeJsonMappingException("Not found!"));
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

        List<Shift> allShifts = this.shiftService.getAll();

        // 31 / (8 * 3) = 24 = 1 remain 7
        // 32 / (7 * 3) = 21 = 1 remain 11
        // 30 / (8 * 3) = 24 = 1 remain 6
        // 30 / (7 * 3) = 21 = 1 remain 9

        // remain = 6 / 3 = 2 days = 2 groups, 1 group for all day have 3 shift that
        // have quantity +1, the rest is base

        if (remainingProducts % SHIFT_PER_DAY == 0) {
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, (remainingProducts / SHIFT_PER_DAY) - 1);
            scheduleCampaigns.add(new ScheduleCampaign(planCampaign, baseProductPerShift + 1, startDate,
                    calendar.getTime(),
                    allShifts));
            calendar.add(Calendar.DATE, 1);
            scheduleCampaigns.add(new ScheduleCampaign(planCampaign, baseProductPerShift,
                    calendar.getTime(), endDate,
                    allShifts));

        } else {
            calendar.setTime(startDate);
            // remain = x / 3 => y remain z shifts
            // remain = 11 / 3 => 3 remain 2 shifts
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, (remainingProducts / SHIFT_PER_DAY) - 1);
            scheduleCampaigns.add(new ScheduleCampaign(planCampaign, baseProductPerShift + 1, startDate,
                    calendar.getTime(),
                    allShifts));

            int remainShifts = remainingProducts % SHIFT_PER_DAY;
            calendar.add(Calendar.DATE, 1);

            List<Shift> firstGroupShift = new ArrayList<>();
            List<Shift> secondGroupShift = new ArrayList<>();

            System.out.println("remaign shift: " + remainShifts + "day: " + remainingProducts);

            if (remainShifts == 1) {
                firstGroupShift.add(shiftService.findShiftByIndex(1));
                secondGroupShift.add(shiftService.findShiftByIndex(2));
                secondGroupShift.add(shiftService.findShiftByIndex(3));
            } else {
                // remainShift == 2
                firstGroupShift.add(shiftService.findShiftByIndex(1));
                firstGroupShift.add(shiftService.findShiftByIndex(2));
                secondGroupShift.add(shiftService.findShiftByIndex(3));
            }

            scheduleCampaigns.add(new ScheduleCampaign(planCampaign, baseProductPerShift + 1, calendar
                    .getTime(),
                    calendar.getTime(),
                    firstGroupShift));
            scheduleCampaigns.add(new ScheduleCampaign(planCampaign, baseProductPerShift, calendar
                    .getTime(),
                    calendar.getTime(),
                    secondGroupShift));

            calendar.add(Calendar.DATE, 1);
            scheduleCampaigns.add(new ScheduleCampaign(planCampaign, baseProductPerShift, calendar
                    .getTime(),
                    endDate,
                    allShifts));
        }

        return scheduleCampaigns;
    }
}

package com.example.scheduling_system.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.scheduling_system.dto.payload.request.SchedulePlanRequest;
import com.example.scheduling_system.dto.payload.request.ScheduleRequest;
import com.example.scheduling_system.models.Employee;
import com.example.scheduling_system.models.Plan;
import com.example.scheduling_system.models.PlanProductMapping;
import com.example.scheduling_system.models.Schedule;
import com.example.scheduling_system.models.Shift;
import com.example.scheduling_system.repositories.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PlanService planService;
    private final ProductService productService;
    private final EmployeeService empService;
    private final ShiftService shiftService;
    private final PlanProductMappingService planProductMappingService;

    private final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    public Schedule create(ScheduleRequest scheduleRequest) {
        var plan = planService.findById(scheduleRequest.planId());
        var product = productService.findById(scheduleRequest.prodId());
        var emp = empService.findById(scheduleRequest.empId());
        var shift = shiftService.findById(scheduleRequest.shiftId());

        return new Schedule(plan, product, emp, shift, scheduleRequest.date());
    }

    private List<PlanProductMapping> sortByEstimateEffort(List<PlanProductMapping> planProductMappings) {
        return planProductMappings.stream().sorted(Comparator.<PlanProductMapping>comparingDouble(
                p -> p.getPlanProductItem().getProduct().getEstimatedEffort())).collect(Collectors.toList());
    }

    private int getDaysFromPlan(Plan plan) {
        return (int) TimeUnit.DAYS.convert(plan.getEndDate().getTime() - plan.getStartDate().getTime(),
                TimeUnit.MILLISECONDS);
    }

    private Date addDateByIndex(Plan plan, int i) {
        calendar.add(Calendar.DAY_OF_MONTH, i);
        return calendar.getTime();
    }

    private void setDateByShift(Shift shift) {
        calendar.set(Calendar.HOUR_OF_DAY, shift.getStartTime().getHour());
        calendar.set(Calendar.MINUTE, shift.getStartTime().getMinute());
        calendar.set(Calendar.SECOND, shift.getStartTime().getSecond());
    }

    private List<PlanProductMapping> getSortedPlanProductByEffort(Plan plan) {
        return sortByEstimateEffort(planProductMappingService.getByPlanId(plan.getId()));
    }

    public void schedule(SchedulePlanRequest request, Plan plan) {
        int days = getDaysFromPlan(plan);
        List<Schedule> schedules = new ArrayList<>();

        Shift shiftS1 = shiftService.findById(1L);
        // Shift shiftS2 = shiftService.findById(2L);
        // Shift shiftS3 = shiftService.findById(3L);
        List<Employee> empS1 = empService.findByIds(request.s1());

        List<PlanProductMapping> sortedPlanProduct = getSortedPlanProductByEffort(plan);

        calendar.setTime(plan.getStartDate());
        setDateByShift(shiftS1);
        for (int i = 0; i < days; i++) {
            Date schDate = addDateByIndex(plan, i);
            ScheduleDay scheduleDay = new ScheduleDay(
                    empS1, sortedPlanProduct, schedules, plan, shiftS1, schDate);
            scheduleDay.schedule();
        }

        scheduleRepository.saveAll(schedules);
        plan.setStatus(Plan.Status.DONE);
        plan.setSchedules(schedules);
    }
}

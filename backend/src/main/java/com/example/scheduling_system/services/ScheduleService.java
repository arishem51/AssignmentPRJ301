package com.example.scheduling_system.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.dto.payload.request.SchedulePlanRequest;
import com.example.scheduling_system.dto.payload.request.ScheduleRequest;
import com.example.scheduling_system.models.Employee;
import com.example.scheduling_system.models.Plan;
import com.example.scheduling_system.models.PlanProductItem;
import com.example.scheduling_system.models.Product;
import com.example.scheduling_system.models.Schedule;
import com.example.scheduling_system.models.Shift;
import com.example.scheduling_system.repositories.ScheduleRepository;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PlanService planService;

    @Autowired
    ProductService productService;

    @Autowired
    EmployeeService empService;

    @Autowired
    ShiftService shiftService;

    final private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    public Schedule create(ScheduleRequest scheduleRequest) {
        var plan = planService.findById(scheduleRequest.planId());
        var product = productService.findById(scheduleRequest.prodId());
        var emp = empService.findById(scheduleRequest.empId());
        var shift = shiftService.findById(scheduleRequest.shiftId());

        return new Schedule(plan, product, emp, shift, scheduleRequest.date());
    }

    private List<PlanProductItem> clonePlanProducts(List<PlanProductItem> planProductItems) {
        List<PlanProductItem> clonedPlanProducts = new ArrayList<>();
        for (PlanProductItem original : planProductItems) {
            clonedPlanProducts.add(original.clone());
        }
        return clonedPlanProducts;
    }

    private List<PlanProductItem> sortPlanProductItem(List<PlanProductItem> planProductItems) {
        Collections.sort(
                planProductItems,
                Comparator.<PlanProductItem>comparingDouble(p -> p.getProduct().getEstimatedEffort()));
        return planProductItems;
    }

    public void schedule(SchedulePlanRequest request, Plan plan) {
        long days = (int) TimeUnit.DAYS.convert(plan.getEndDate().getTime() - plan.getStartDate().getTime(),
                TimeUnit.MILLISECONDS);
        List<Schedule> schedules = new ArrayList<>();

        Shift shiftS1 = shiftService.findById(1L);
        Shift shiftS2 = shiftService.findById(2L);
        Shift shiftS3 = shiftService.findById(3L);

        List<PlanProductItem> sortedPlanProduct = this.sortPlanProductItem(
                this.clonePlanProducts(plan.getPlanProducts()));

        for (int i = 0; i < days; i++) {
            calendar.setTime(plan.getStartDate());
            calendar.add(Calendar.DAY_OF_MONTH, i);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date schDate = calendar.getTime();

            for (Long empId : request.s1()) {
                double availableTime = 8;
                Employee e = empService.findById(empId);
                double workingRate = e.getWorkingRate();

                if (availableTime == 0) {
                    continue;
                }

                for (PlanProductItem planProductItem : sortedPlanProduct) {
                    Product currentProduct = planProductItem.getProduct();
                    double actualEffort = currentProduct.getEstimatedEffort() / workingRate;
                    double quantity = planProductItem.getQuantity();

                    double pieceCount = (int) (availableTime / actualEffort);
                    if (pieceCount > 0 && quantity > 0) {
                        pieceCount = quantity < pieceCount ? quantity : Math.min(pieceCount, (int) quantity);

                        Schedule sch = new Schedule(plan, currentProduct, e, shiftS1,
                                schDate);
                        sch.setQuantity(pieceCount);
                        availableTime -= (pieceCount * actualEffort);
                        planProductItem.setQuantity(planProductItem.getQuantity() - pieceCount);
                        schedules.add(sch);
                    }
                }

                var largestEffortPlanProductItem = sortedPlanProduct.get(0);
                if (availableTime > 0 && largestEffortPlanProductItem.getQuantity() > 0) {
                    Schedule sch = new Schedule(plan,
                            largestEffortPlanProductItem.getProduct(), e, shiftS1,
                            schDate);
                    Product largestProduct = sch.getProduct();
                    double actualEffort = largestProduct.getEstimatedEffort() / workingRate;
                    double partialQuantity = availableTime / actualEffort;
                    // Ensure partial quantity does not exceed available quantity
                    partialQuantity = Math.min(partialQuantity, largestEffortPlanProductItem.getQuantity());
                    sch.setQuantity(sch.getQuantity() + partialQuantity);
                    schedules.add(sch);

                    double leftQuantity = largestEffortPlanProductItem.getQuantity() - partialQuantity;
                    largestEffortPlanProductItem
                            .setQuantity(leftQuantity);
                    availableTime = 0;
                }
            }
        }

        plan.setStatus(Plan.Status.DONE);
        scheduleRepository.saveAll(schedules);
        plan.setSchedules(schedules);
    }
}

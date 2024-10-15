package com.example.scheduling_system.services;

import com.example.scheduling_system.models.Employee;
import com.example.scheduling_system.models.Plan;
import com.example.scheduling_system.models.PlanProductMapping;
import com.example.scheduling_system.models.Product;
import com.example.scheduling_system.models.Schedule;
import com.example.scheduling_system.models.Shift;
import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleDay {
    private final List<Employee> emps;
    private final List<PlanProductMapping> planProducts;
    private final List<Schedule> schedules;
    private final Plan plan;
    private final Shift shift;
    private final Date scheduleDate;

    private double calculateActualEffort(Product product, Employee emp) {
        return product.getEstimatedEffort() / emp.getWorkingRate();
    }

    public void schedule() {
        for (Employee e : emps) {
            double availableTime = 8;
            PlanProductMapping lastPlanProductWorking = null;

            if (availableTime > 0) {
                for (PlanProductMapping planProductItem : planProducts) {
                    Product currentProduct = planProductItem.getPlanProductItem().getProduct();
                    double actualEffort = calculateActualEffort(currentProduct, e);
                    double leftQuantity = planProductItem.leftQuantity();

                    double pieceCount = (int) (availableTime / actualEffort);
                    if (pieceCount > 0 && !planProductItem.isEnoughQuantity()) {
                        pieceCount = leftQuantity < pieceCount ? leftQuantity : pieceCount;
                        lastPlanProductWorking = planProductItem;
                        Schedule sch = new Schedule(plan, currentProduct, e,
                                shift,
                                scheduleDate);
                        sch.setQuantity(pieceCount);
                        availableTime -= (pieceCount * actualEffort);
                        planProductItem.setQuantity(planProductItem.getQuantity() + pieceCount);
                        schedules.add(sch);
                    }

                }

                if (availableTime > 0 && lastPlanProductWorking != null && !lastPlanProductWorking.isEnoughQuantity()) {
                    Schedule sch = new Schedule(plan,
                            lastPlanProductWorking.getPlanProductItem().getProduct(), e,
                            shift,
                            scheduleDate);
                    double actualEffort = calculateActualEffort(sch.getProduct(), e);

                    double partialQuantity = availableTime / actualEffort;
                    // Ensure partial quantity does not exceed available quantity
                    partialQuantity = Math.min(partialQuantity,
                            lastPlanProductWorking.leftQuantity());
                    sch.setQuantity(sch.getQuantity() + partialQuantity);
                    schedules.add(sch);

                    double leftQuantity = lastPlanProductWorking.getQuantity() + partialQuantity;
                    lastPlanProductWorking
                            .setQuantity(leftQuantity);
                    availableTime = 0;
                }
            }
        }
    }
}

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
            if (availableTime > 0) {
                for (PlanProductMapping planProductItem : planProducts) {
                    Product currentProduct = planProductItem.getPlanProductItem().getProduct();
                    double actualEffort = calculateActualEffort(currentProduct, e);
                    double leftQuantity = planProductItem.leftQuantity();

                    double pieceCount = (int) (availableTime / actualEffort);
                    if (pieceCount > 0 && !planProductItem.isEnoughQuantity()) {
                        pieceCount = leftQuantity < pieceCount ? leftQuantity : pieceCount;

                        Schedule sch = new Schedule(plan, currentProduct, e,
                                shift,
                                scheduleDate);
                        sch.setQuantity(pieceCount);
                        availableTime -= (pieceCount * actualEffort);
                        planProductItem.setQuantity(planProductItem.getQuantity() + pieceCount);
                        schedules.add(sch);
                    }

                }

                var largestEffortPlanProductItem = planProducts.get(0);
                if (availableTime > 0 && !largestEffortPlanProductItem.isEnoughQuantity()) {
                    Schedule sch = new Schedule(plan,
                            largestEffortPlanProductItem.getPlanProductItem().getProduct(), e,
                            shift,
                            scheduleDate);
                    double actualEffort = calculateActualEffort(sch.getProduct(), e);

                    double partialQuantity = availableTime / actualEffort;
                    // Ensure partial quantity does not exceed available quantity
                    partialQuantity = Math.min(partialQuantity,
                            largestEffortPlanProductItem.leftQuantity());
                    sch.setQuantity(sch.getQuantity() + partialQuantity);
                    schedules.add(sch);

                    double leftQuantity = largestEffortPlanProductItem.getQuantity() + partialQuantity;
                    largestEffortPlanProductItem
                            .setQuantity(leftQuantity);
                    availableTime = 0;
                }
            }
        }
    }
}

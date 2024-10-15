package com.example.scheduling_system.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduling_system.dto.payload.request.PlanRequest;
import com.example.scheduling_system.dto.payload.request.SchedulePlanRequest;
import com.example.scheduling_system.dto.payload.response.BodyResponse;
import com.example.scheduling_system.dto.payload.response.ListPlanResponseItem;
import com.example.scheduling_system.dto.payload.response.PaginateResponse;
import com.example.scheduling_system.services.PlanService;
import com.example.scheduling_system.services.ScheduleService;

@RestController
@RequestMapping(path = "api/plans")
public class PlanController {
    @Autowired
    private PlanService planService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<BodyResponse<PaginateResponse<ListPlanResponseItem>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String search) {
        try {
            PaginateResponse<ListPlanResponseItem> plans = planService.findAll(PageRequest.of(page, pageSize), search);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", plans));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Get plan error!"));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PlanRequest request) {
        try {
            var plan = planService.create(request);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", plan));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Create plan error!"));
        }
    }

    @PostMapping("/{id}/schedule")
    public ResponseEntity<?> schedule(@PathVariable Long id, @RequestBody SchedulePlanRequest request) {
        try {
            var plan = planService.findById(id);
            // Find the common productIds between 3 sets products ids
            Set<Long> commonElement = new HashSet<>(request.s1());
            commonElement.retainAll(request.s2());
            commonElement.retainAll(request.s3());

            if (!commonElement.isEmpty()) {
                return ResponseEntity.ok()
                        .body(new BodyResponse<>("An employee can work 2 shifts a day!", null));
            }

            scheduleService.schedule(request, plan);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PlanRequest request) {
        try {
            var plan = planService.update(id, request);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", plan));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>(e.getMessage()));
        }
    }
}

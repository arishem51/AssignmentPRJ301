package com.example.scheduling_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduling_system.payload.request.PlanProductItemRequest;
import com.example.scheduling_system.payload.response.BodyResponse;
import com.example.scheduling_system.services.PlanProductItemService;

@RestController
@RequestMapping("/api/plan-product-items")
public class PlanProductItemController {
    @Autowired
    PlanProductItemService planProductItemService;

    @PostMapping
    public ResponseEntity<?> createPlanProductItem(@RequestBody PlanProductItemRequest request) {
        try {
            var product = planProductItemService.create(request);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", product).getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Create plan product item error!").getBodyResponse());
        }
    }
}

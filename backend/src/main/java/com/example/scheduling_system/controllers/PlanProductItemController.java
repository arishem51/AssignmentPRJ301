package com.example.scheduling_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduling_system.dto.payload.request.PlanProductItemRequest;
import com.example.scheduling_system.dto.payload.response.BodyResponse;
import com.example.scheduling_system.services.PlanProductItemService;

@RestController
@RequestMapping("/api/plan-product-items")
public class PlanProductItemController {
    @Autowired
    PlanProductItemService planProductItemService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PlanProductItemRequest request) {
        try {
            var product = planProductItemService.create(request);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", product).getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Create plan product item error!").getBodyResponse());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String search) {
        try {
            var results = planProductItemService.findAll(PageRequest.of(page, pageSize), search);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", results).getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Create plan product item error!").getBodyResponse());
        }
    }
}

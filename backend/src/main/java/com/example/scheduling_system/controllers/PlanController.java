package com.example.scheduling_system.controllers;

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

import com.example.scheduling_system.payload.request.PlanRequest;
import com.example.scheduling_system.payload.response.BodyResponse;
import com.example.scheduling_system.services.PlanService;

@RestController
@RequestMapping(path = "api/plans")
public class PlanController {
    @Autowired
    private PlanService planService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String search) {
        try {
            var plans = planService.findAll(PageRequest.of(page, pageSize), search);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", plans).getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Get plan error!").getBodyResponse());
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PlanRequest request) {
        try {
            var plan = planService.create(request);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", plan).getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Create plan error!").getBodyResponse());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PlanRequest request) {
        try {
            var plan = planService.update(id, request);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", plan).getBodyResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Create plan error!").getBodyResponse());
        }
    }
}

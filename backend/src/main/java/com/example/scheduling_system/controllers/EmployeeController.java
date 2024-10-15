package com.example.scheduling_system.controllers;

import com.example.scheduling_system.dto.payload.request.EmployeeRequest;
import com.example.scheduling_system.dto.payload.response.BodyResponse;
import com.example.scheduling_system.services.EmployeeService;

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

@RestController
@RequestMapping(path = "api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String search) {
        try {
            var plans = employeeService.findAll(PageRequest.of(page, pageSize), search);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", plans));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Get product error!"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody EmployeeRequest request) {
        try {
            var plan = employeeService.create(request);
            return ResponseEntity.ok().body(new BodyResponse<>("Success", plan));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BodyResponse<>("Create product error!"));
        }
    }
}

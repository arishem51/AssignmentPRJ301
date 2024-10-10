package com.example.scheduling_system.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.dto.Meta;
import com.example.scheduling_system.models.Employee;
import com.example.scheduling_system.payload.request.EmployeeRequest;
import com.example.scheduling_system.payload.response.PaginateResponse;
import com.example.scheduling_system.repositories.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee create(EmployeeRequest request) {
        var plan = new Employee(request.name(), request.hourlyWage(), request.role());
        return employeeRepository.save(plan);
    }

    public PaginateResponse<Employee> findAll(Pageable pageable, String search) {
        var page = search.isEmpty() ? employeeRepository.findAll(pageable)
                : employeeRepository.findByNameContainingIgnoreCase(search, pageable);
        List<Employee> responses = page.getContent().stream().collect(Collectors.toList());
        Meta meta = new Meta(page.getNumber() + 1, page.getSize(), page.getTotalElements(),
                page.getTotalPages());
        return new PaginateResponse<>(responses, meta);
    }
}

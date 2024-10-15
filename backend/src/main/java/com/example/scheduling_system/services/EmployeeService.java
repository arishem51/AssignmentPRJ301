package com.example.scheduling_system.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.scheduling_system.dto.Meta;
import com.example.scheduling_system.dto.payload.request.EmployeeRequest;
import com.example.scheduling_system.dto.payload.response.PaginateResponse;
import com.example.scheduling_system.models.Employee;
import com.example.scheduling_system.repositories.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

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

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(new Error("Employee not found!")));
    }

    public List<Employee> findByIds(Set<Long> ids) {
        return employeeRepository.findAllById(ids);
    }
}

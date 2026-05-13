package com.nihalsingh.intern.EmployeeManagement.controller;

import com.nihalsingh.intern.EmployeeManagement.dto.AddEmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.dto.EmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getEmployees(
            @RequestParam(required = false) String sortBy) {
        if (sortBy != null) {
            return ResponseEntity.ok(employeeService.getSortedEmployees(sortBy));
        }
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto >getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok( employeeService.getAllEmployeeByID(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody @Valid AddEmployeeDto addEmployeeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createNewEmployee(addEmployeeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,
                                                      @RequestBody AddEmployeeDto addEmployeeDto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id,addEmployeeDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,
                                                      @RequestBody Map<String,Object> updates) {
        return ResponseEntity.ok(employeeService.updatePartialEmployee(id,updates));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDto>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {
        if (name != null) {
            return ResponseEntity.ok(employeeService.searchByName(name));
        } else if (department != null) {
            return ResponseEntity.ok(employeeService.searchByDepartment(department));
        }
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}
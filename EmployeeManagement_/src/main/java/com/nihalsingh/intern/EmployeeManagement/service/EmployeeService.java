package com.nihalsingh.intern.EmployeeManagement.service;

import com.nihalsingh.intern.EmployeeManagement.dto.AddEmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.dto.EmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.entity.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    List<EmployeeDto> getAllEmployees();

    EmployeeDto getAllEmployeeByID(Long id);

    EmployeeDto createNewEmployee(AddEmployeeDto addEmployeeDto);

    void deleteEmployeeById(Long id);

    EmployeeDto updateEmployee(Long id, AddEmployeeDto addEmployeeDto);

    EmployeeDto updatePartialEmployee(Long id, Map<String, Object> updates);

    List<EmployeeDto> searchByName(String name);

    List<EmployeeDto> searchByDepartment(String department);

    List<EmployeeDto> getSortedEmployees(String sortBy);
}
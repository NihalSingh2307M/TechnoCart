package com.nihalsingh.intern.EmployeeManagement.service.impl;
import com.nihalsingh.intern.EmployeeManagement.dto.AddEmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.dto.EmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.entity.Employee;
import com.nihalsingh.intern.EmployeeManagement.repository.EmployeeRepository;
import com.nihalsingh.intern.EmployeeManagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }

    @Override
    public EmployeeDto getAllEmployeeByID(Long id) {
        Employee employee =  employeeRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Employee not found with ID:"+id));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto createNewEmployee(AddEmployeeDto addEmployeeDto) {

        Employee newemployee = modelMapper.map(addEmployeeDto, Employee.class);
        Employee employee = employeeRepository.save(newemployee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        if(!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Employee not found with ID:"+id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, AddEmployeeDto addEmployeeDto) {
        Employee employee =  employeeRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Employee not found with ID:"+id));
        modelMapper.map(addEmployeeDto, employee);
        employee = employeeRepository.save(employee);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updatePartialEmployee(Long id, Map<String, Object> updates) {

        Employee employee =  employeeRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Employee not found with ID:"+id));
        updates.forEach((field,value)->{
            switch (field) {

                case "firstName":
                    employee.setFirstName((String) value);
                    break;

                case "lastName":
                    employee.setLastName((String) value);
                    break;

                case "email":
                    employee.setEmail((String) value);
                    break;

                case "department":
                    employee.setDepartment((String) value);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown field: " + field);
            }
        });
        Employee savedemployee =  employeeRepository.save(employee);
        return modelMapper.map(savedemployee, EmployeeDto.class);
    }

    @Override
    public List<EmployeeDto> searchByName(String name) {
        return employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }

    @Override
    public List<EmployeeDto> searchByDepartment(String department) {
        return employeeRepository
                .findByDepartmentIgnoreCase(department)
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }

    @Override
    public List<EmployeeDto> getSortedEmployees(String sortBy) {
        Comparator<Employee> comparator;
        switch (sortBy.toLowerCase()) {
            case "name":
                comparator = Comparator.comparing(Employee::getFirstName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Employee::getLastName, String.CASE_INSENSITIVE_ORDER);
                break;
            case "id":
            default:
                comparator = Comparator.comparing(Employee::getId);
                break;
        }
        return employeeRepository.findAll()
                .stream()
                .sorted(comparator)
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }
}
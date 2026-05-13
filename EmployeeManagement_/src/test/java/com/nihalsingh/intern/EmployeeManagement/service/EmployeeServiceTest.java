package com.nihalsingh.intern.EmployeeManagement.service;

import com.nihalsingh.intern.EmployeeManagement.dto.AddEmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.dto.EmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.entity.Employee;
import com.nihalsingh.intern.EmployeeManagement.repository.EmployeeRepository;
import com.nihalsingh.intern.EmployeeManagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock EmployeeRepository employeeRepository;
    @Mock ModelMapper modelMapper;
    @InjectMocks EmployeeServiceImpl employeeService;

    private Employee makeEmployee() {
        Employee e = new Employee();
        e.setId(1L);
        e.setFirstName("Alice");
        e.setLastName("Smith");
        e.setEmail("alice@example.com");
        e.setDepartment("Engineering");
        return e;
    }

    private EmployeeDto makeDto() {
        return new EmployeeDto(1L, "Alice", "Smith", "alice@example.com", "Engineering");
    }

    @Test
    void getAll_returnsEmployeeList() {
        Employee e = makeEmployee();
        EmployeeDto dto = makeDto();
        given(employeeRepository.findAll()).willReturn(List.of(e));
        given(modelMapper.map(e, EmployeeDto.class)).willReturn(dto);

        assertThat(employeeService.getAllEmployees()).containsExactly(dto);
    }

    @Test
    void getById_returnsEmployee_whenExists() {
        Employee e = makeEmployee();
        given(employeeRepository.findById(1L)).willReturn(Optional.of(e));
        given(modelMapper.map(e, EmployeeDto.class)).willReturn(makeDto());

        assertThat(employeeService.getAllEmployeeByID(1L).getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void getById_throws_whenNotFound() {
        given(employeeRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getAllEmployeeByID(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_savesAndReturnsEmployee() {
        AddEmployeeDto addDto = new AddEmployeeDto();
        addDto.setFirstName("Alice");
        addDto.setEmail("alice@example.com");

        Employee e = makeEmployee();
        given(modelMapper.map(addDto, Employee.class)).willReturn(e);
        given(employeeRepository.save(e)).willReturn(e);
        given(modelMapper.map(e, EmployeeDto.class)).willReturn(makeDto());

        assertThat(employeeService.createNewEmployee(addDto).getFirstName()).isEqualTo("Alice");
    }

    @Test
    void delete_succeeds_whenExists() {
        given(employeeRepository.existsById(1L)).willReturn(true);

        assertThatNoException().isThrownBy(() -> employeeService.deleteEmployeeById(1L));
        then(employeeRepository).should().deleteById(1L);
    }

    @Test
    void delete_throws_whenNotFound() {
        given(employeeRepository.existsById(99L)).willReturn(false);

        assertThatThrownBy(() -> employeeService.deleteEmployeeById(99L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
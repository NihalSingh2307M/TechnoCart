package com.nihalsingh.intern.EmployeeManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nihalsingh.intern.EmployeeManagement.dto.AddEmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.dto.EmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean EmployeeService employeeService;

    private EmployeeDto makeDto() {
        return new EmployeeDto(1L, "Alice", "Smith", "alice@example.com", "Engineering");
    }

    @Test
    void getAll_returns200_withEmployeeList() throws Exception {
        given(employeeService.getAllEmployees()).willReturn(List.of(makeDto()));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"));
    }

    @Test
    void getById_returns200_withEmployee() throws Exception {
        given(employeeService.getAllEmployeeByID(1L)).willReturn(makeDto());

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void create_returns201_withValidBody() throws Exception {
        AddEmployeeDto addDto = new AddEmployeeDto();
        addDto.setFirstName("Alice");
        addDto.setLastName("Smith");
        addDto.setEmail("alice@example.com");
        addDto.setDepartment("Engineering");

        given(employeeService.createNewEmployee(any())).willReturn(makeDto());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_returns400_whenFirstNameBlank() throws Exception {
        AddEmployeeDto addDto = new AddEmployeeDto();
        addDto.setFirstName("");
        addDto.setEmail("alice@example.com");

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returns400_whenEmailInvalid() throws Exception {
        AddEmployeeDto addDto = new AddEmployeeDto();
        addDto.setFirstName("Alice");
        addDto.setEmail("not-an-email");

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_returns204() throws Exception {
        willDoNothing().given(employeeService).deleteEmployeeById(1L);

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());
    }
}
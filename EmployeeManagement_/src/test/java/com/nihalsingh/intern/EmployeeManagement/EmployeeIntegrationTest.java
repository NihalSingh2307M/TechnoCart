package com.nihalsingh.intern.EmployeeManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nihalsingh.intern.EmployeeManagement.dto.AddEmployeeDto;
import com.nihalsingh.intern.EmployeeManagement.entity.Employee;
import com.nihalsingh.intern.EmployeeManagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired EmployeeRepository employeeRepository;

    @BeforeEach
    void cleanUp() {
        employeeRepository.deleteAll();
    }

    private AddEmployeeDto makeDto(String first, String last, String email, String dept) {
        AddEmployeeDto dto = new AddEmployeeDto();
        dto.setFirstName(first);
        dto.setLastName(last);
        dto.setEmail(email);
        dto.setDepartment(dept);
        return dto;
    }

    private Employee seedEmployee(String first, String last, String email, String dept) {
        Employee e = new Employee();
        e.setFirstName(first);
        e.setLastName(last);
        e.setEmail(email);
        e.setDepartment(dept);
        return employeeRepository.save(e);
    }

    // ── CREATE ────────────────────────────────────────────────────
    @Test
    void createEmployee_returns201_andPersists() throws Exception {
        AddEmployeeDto dto = makeDto("Alice", "Smith", "alice@example.com", "Engineering");

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void createEmployee_returns400_whenFirstNameBlank() throws Exception {
        AddEmployeeDto dto = makeDto("", "Smith", "alice@example.com", "Engineering");

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createEmployee_returns400_whenEmailInvalid() throws Exception {
        AddEmployeeDto dto = makeDto("Alice", "Smith", "not-an-email", "Engineering");

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ── READ ──────────────────────────────────────────────────────
    @Test
    void getAllEmployees_returnsSeededList() throws Exception {
        seedEmployee("Alice", "Smith", "alice@example.com", "Engineering");
        seedEmployee("Bob", "Brown", "bob@example.com", "HR");

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getEmployeeById_returnsCorrectEmployee() throws Exception {
        Employee saved = seedEmployee("Alice", "Smith", "alice@example.com", "Engineering");

        mockMvc.perform(get("/employees/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    @Test
    void getEmployeeById_returns404_whenNotFound() throws Exception {
        mockMvc.perform(get("/employees/9999"))
                .andExpect(status().isNotFound())  // ✅ 404 now (GlobalExceptionHandler)
                .andExpect(jsonPath("$.message").value(containsString("9999")));
    }

    // ── UPDATE ────────────────────────────────────────────────────
    @Test
    void updateEmployee_returns200_andUpdatesData() throws Exception {
        Employee saved = seedEmployee("Alice", "Smith", "alice@example.com", "Engineering");
        AddEmployeeDto updated = makeDto("AliceUpdated", "Smith", "alice@example.com", "HR");

        mockMvc.perform(put("/employees/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("AliceUpdated"))
                .andExpect(jsonPath("$.department").value("HR"));
    }

    @Test
    void patchEmployee_returns200_andUpdatesField() throws Exception {
        Employee saved = seedEmployee("Alice", "Smith", "alice@example.com", "Engineering");

        String patch = """
                { "department": "Finance" }
                """;

        mockMvc.perform(patch("/employees/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patch))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("Finance"))
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    // ── DELETE ────────────────────────────────────────────────────
    @Test
    void deleteEmployee_returns204_andRemovesFromDb() throws Exception {
        Employee saved = seedEmployee("Alice", "Smith", "alice@example.com", "Engineering");

        mockMvc.perform(delete("/employees/" + saved.getId()))
                .andExpect(status().isNoContent());

        // confirm deleted → now returns 404 ✅
        mockMvc.perform(get("/employees/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    // ── SEARCH ────────────────────────────────────────────────────
    @Test
    void searchByName_returnsMatchingEmployees() throws Exception {
        seedEmployee("Alice", "Smith", "alice@example.com", "Engineering");
        seedEmployee("Bob", "Brown", "bob@example.com", "HR");

        mockMvc.perform(get("/employees/search?name=alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Alice"));
    }

    @Test
    void searchByDepartment_returnsMatchingEmployees() throws Exception {
        seedEmployee("Alice", "Smith", "alice@example.com", "Engineering");
        seedEmployee("Bob", "Brown", "bob@example.com", "HR");
        seedEmployee("Carol", "White", "carol@example.com", "Engineering");

        mockMvc.perform(get("/employees/search?department=Engineering"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    // ── SORT ──────────────────────────────────────────────────────
    @Test
    void getSortedByName_returnsAlphabeticalOrder() throws Exception {
        seedEmployee("Charlie", "X", "c@example.com", "IT");
        seedEmployee("Alice", "X", "a@example.com", "IT");
        seedEmployee("Bob", "X", "b@example.com", "IT");

        mockMvc.perform(get("/employees?sortBy=name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].firstName").value("Bob"))
                .andExpect(jsonPath("$[2].firstName").value("Charlie"));
    }
}
package com.nihalsingh.intern.EmployeeManagement.repository;

import com.nihalsingh.intern.EmployeeManagement.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired EmployeeRepository employeeRepository;

    private Employee makeEmployee(String first, String last, String email, String dept) {
        Employee e = new Employee();
        e.setFirstName(first);
        e.setLastName(last);
        e.setEmail(email);
        e.setDepartment(dept);
        return e;
    }

    @BeforeEach
    void seed() {
        employeeRepository.deleteAll();
        employeeRepository.saveAll(List.of(
                makeEmployee("Alice", "Smith", "alice@example.com", "Engineering"),
                makeEmployee("Bob",   "Brown", "bob@example.com",   "HR"),
                makeEmployee("Carol", "Smith", "carol@example.com", "Engineering")
        ));
    }

    @Test
    void findByName_returnsMatches_caseInsensitive() {
        List<Employee> result = employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("alice", "alice");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alice");
    }

    @Test
    void findByName_matchesLastName() {
        List<Employee> result = employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("smith", "smith");

        assertThat(result).hasSize(2);
    }

    @Test
    void findByDepartment_returnsCorrectEmployees() {
        List<Employee> result = employeeRepository.findByDepartmentIgnoreCase("ENGINEERING");

        assertThat(result).hasSize(2)
                .extracting(Employee::getDepartment)
                .containsOnly("Engineering");
    }

    @Test
    void findByDepartment_returnsEmpty_whenNoneMatch() {
        List<Employee> result = employeeRepository.findByDepartmentIgnoreCase("Finance");

        assertThat(result).isEmpty();
    }

    @Test
    void save_assignsId() {
        Employee saved = employeeRepository.save(
                makeEmployee("Dave", "Jones", "dave@example.com", "Finance"));

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void existsById_returnsFalse_whenDeleted() {
        Employee saved = employeeRepository.save(
                makeEmployee("Eve", "White", "eve@example.com", "IT"));
        employeeRepository.deleteById(saved.getId());

        assertThat(employeeRepository.existsById(saved.getId())).isFalse();
    }
}
package com.nihalsingh.intern.EmployeeManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEmployeeDto {
    @NotBlank(message = "Name is Required")
    @Size(min = 4, max = 30, message = "Name should be b/w 4-30 chars")
    private String firstName;
    private String lastName;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    private String department;
}
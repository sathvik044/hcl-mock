package com.example.CorporateTravelManagementSystem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String role;
    private String employeeId;
    private Long managerId;
}

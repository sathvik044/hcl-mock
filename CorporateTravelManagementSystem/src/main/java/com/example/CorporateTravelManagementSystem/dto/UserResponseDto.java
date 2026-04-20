package com.example.CorporateTravelManagementSystem.dto;

import java.time.LocalDateTime;

import com.example.CorporateTravelManagementSystem.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private String department;
    private String costCenter;
    private Long managerId;
    private String managerName;
    private LocalDateTime createdAt;
}

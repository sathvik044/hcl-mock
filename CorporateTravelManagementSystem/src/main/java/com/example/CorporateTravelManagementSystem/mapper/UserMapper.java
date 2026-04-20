package com.example.CorporateTravelManagementSystem.mapper;

import com.example.CorporateTravelManagementSystem.dto.UserDTO;
import com.example.CorporateTravelManagementSystem.dto.UserRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .role(user.getRole())
                .employeeId(user.getEmployeeId())
                .managerId(user.getManagerId())
                .build();
    }

    public User toEntity(UserRequestDTO userRequestDTO) {
        return User.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .email(userRequestDTO.getEmail())
                .department(userRequestDTO.getDepartment())
                .role(userRequestDTO.getRole())
                .employeeId(userRequestDTO.getEmployeeId())
                .managerId(userRequestDTO.getManagerId())
                .build();
    }
}

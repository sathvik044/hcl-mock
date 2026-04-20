package com.example.CorporateTravelManagementSystem.mapper;

import org.springframework.stereotype.Component;

import com.example.CorporateTravelManagementSystem.dto.UserRequestDto;
import com.example.CorporateTravelManagementSystem.dto.UserResponseDto;
import com.example.CorporateTravelManagementSystem.entity.User;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setRole(userRequestDto.getRole());
        user.setDepartment(userRequestDto.getDepartment());
        user.setCostCenter(userRequestDto.getCostCenter());
        return user;
    }

    public UserResponseDto toResponseDto(User user) {
        Long managerId = null;
        String managerName = null;

        if (user.getManager() != null) {
            managerId = user.getManager().getId();
            managerName = user.getManager().getName();
        }

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getDepartment(),
                user.getCostCenter(),
                managerId,
                managerName,
                user.getCreatedAt());
    }
}

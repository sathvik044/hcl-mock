package com.example.CorporateTravelManagementSystem.Service;

import com.example.CorporateTravelManagementSystem.dto.UserRequestDto;
import com.example.CorporateTravelManagementSystem.dto.UserResponseDto;
import com.example.CorporateTravelManagementSystem.enums.UserRole;
import java.util.List;

public interface UserService {
    
    UserResponseDto createUser(UserRequestDto userRequestDto);
    List<UserResponseDto> getAllUsers();
    List<UserResponseDto> getUsersByRole(UserRole role);
    List<UserResponseDto> get(String department);
    List<UserResponseDto> getTeam(Long managerId);
}


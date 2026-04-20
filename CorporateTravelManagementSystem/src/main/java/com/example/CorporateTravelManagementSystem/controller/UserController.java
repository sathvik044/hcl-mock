package com.example.CorporateTravelManagementSystem.controller;

import com.example.CorporateTravelManagementSystem.dto.UserRequestDto;
import com.example.CorporateTravelManagementSystem.dto.UserResponseDto;
import com.example.CorporateTravelManagementSystem.enums.UserRole;
import com.example.CorporateTravelManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping
    public List<UserResponseDto> getUsers(@RequestParam(required = false) UserRole role) {
        if (role != null) {
            return userService.getUsersByRole(role);
        }
        return userService.getAllUsers();
    }

    @GetMapping("/{id}/team")
    public List<UserResponseDto> getTeam(@PathVariable Long id) {
        return userService.getTeam(id);
    }
    @GetMapping("/department/{department}")
    public List<UserResponseDto> getUsersByDepartment(@PathVariable String department) {
        return userService.get(department);     
}
}

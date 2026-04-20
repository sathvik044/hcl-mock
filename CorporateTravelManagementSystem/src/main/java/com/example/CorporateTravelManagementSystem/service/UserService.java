package com.example.CorporateTravelManagementSystem.Service;

import com.example.CorporateTravelManagementSystem.dto.UserDTO;
import com.example.CorporateTravelManagementSystem.dto.UserRequestDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAllUsers();

    List<UserDTO> getUsersByRole(String role);

    List<UserDTO> getTeamMembers(Long managerId);

    Optional<UserDTO> getUserById(Long id);

    Optional<UserDTO> getUserByEmail(String email);

    UserDTO createUser(UserRequestDTO userRequestDTO);

    UserDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    void deleteUser(Long id);
}

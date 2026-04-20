package com.example.CorporateTravelManagementSystem.Service.serviceImpl;

import com.example.CorporateTravelManagementSystem.Exception.UserAlreadyExistsException;
import com.example.CorporateTravelManagementSystem.Exception.InvalidUserRoleException;
import com.example.CorporateTravelManagementSystem.Exception.UserNotFoundException;
import com.example.CorporateTravelManagementSystem.dto.UserRequestDto;
import com.example.CorporateTravelManagementSystem.dto.UserResponseDto;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.UserRole;
import com.example.CorporateTravelManagementSystem.mapper.UserMapper;
import com.example.CorporateTravelManagementSystem.Repository.UserRepository;
import com.example.CorporateTravelManagementSystem.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + userRequestDto.getEmail());
        }

        User user = userMapper.toEntity(userRequestDto);
        user.setCreatedAt(LocalDateTime.now());

        if (userRequestDto.getManagerId() != null) {
            User manager = userRepository.findById(userRequestDto.getManagerId())
                    .orElseThrow(() -> new UserNotFoundException(
                            "Manager not found with id: " + userRequestDto.getManagerId()));
            user.setManager(manager);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

 @Override
public List<UserResponseDto> getTeam(Long managerId) {

    User manager = userRepository.findById(managerId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + managerId));

    if (manager.getRole() != UserRole.MANAGER) {
        throw new InvalidUserRoleException("User is not a manager");
    }

    return userRepository.findByManager_Id(manager.getId())
            .stream()
            .map(userMapper::toResponseDto)
            .collect(Collectors.toList());
}

    @Override
    public List<UserResponseDto> get(String department) {
        return userRepository.findByDepartment(department)
                .stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}


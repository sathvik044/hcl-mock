package com.example.CorporateTravelManagementSystem.Service.serviceImpl;

import com.example.CorporateTravelManagementSystem.dto.UserDTO;
import com.example.CorporateTravelManagementSystem.dto.UserRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.mapper.UserMapper;
import com.example.CorporateTravelManagementSystem.Repository.UserRepository;
import com.example.CorporateTravelManagementSystem.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByRole(String role) {
        return userRepository.findByRoleIgnoreCase(role).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getTeamMembers(Long managerId) {
        if (!userRepository.existsById(managerId)) {
            throw new NoSuchElementException("Manager not found");
        }

        return userRepository.findByManagerId(managerId).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO);
    }

    @Override
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        validateManager(userRequestDTO.getManagerId());
        User user = userMapper.toEntity(userRequestDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        validateManager(userRequestDTO.getManagerId());

        User updatedUser = userMapper.toEntity(userRequestDTO);
        updatedUser.setId(id);
        User savedUser = userRepository.save(updatedUser);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private void validateManager(Long managerId) {
        if (managerId != null && !userRepository.existsById(managerId)) {
            throw new NoSuchElementException("Manager not found");
        }
    }
}

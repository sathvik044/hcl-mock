package com.example.CorporateTravelManagementSystem.Controller;

import com.example.CorporateTravelManagementSystem.Repository.UserRepository;
import com.example.CorporateTravelManagementSystem.dto.ApiResponse;
import com.example.CorporateTravelManagementSystem.dto.LoginRequestDto;
import com.example.CorporateTravelManagementSystem.dto.LoginResponseDto;
import com.example.CorporateTravelManagementSystem.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        return userRepository.findByEmail(loginRequest.getEmail())
                .filter(user -> loginRequest.getPassword().equals(user.getPassword()))
                .map(user -> {
                    LoginResponseDto response = new LoginResponseDto(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole(),
                            user.getDepartment(),
                            user.getCostCenter(),
                            "mock-jwt-token"
                    );
                    return ResponseEntity.ok(new ApiResponse<>(response));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}

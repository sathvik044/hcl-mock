package com.example.CorporateTravelManagementSystem.config;

import com.example.CorporateTravelManagementSystem.Repository.UserRepository;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        initializeUsers();
    }

    private void initializeUsers() {
        createUserIfNotFound("Admin User", "admin@corporate.com", "Password@123", UserRole.ADMIN, "IT", "CC001", null);
        
        User manager = createUserIfNotFound("Rajesh Kumar", "rajesh.kumar@corporate.com", "Password@123", UserRole.MANAGER, "Sales", "CC002", null);
        
        createUserIfNotFound("Arjun Mehta", "arjun.mehta@corporate.com", "Password@123", UserRole.EMPLOYEE, "Sales", "CC002", manager);
        createUserIfNotFound("Vikram Singh", "vikram.singh@corporate.com", "Password@123", UserRole.FINANCE, "Finance", "CC003", null);
        createUserIfNotFound("Meera Nair", "meera.nair@corporate.com", "Password@123", UserRole.TRAVEL_DESK, "Admin", "CC004", null);
    }

    private User createUserIfNotFound(String name, String email, String password, UserRole role, String dept, String costCenter, User manager) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);
            user.setDepartment(dept);
            user.setCostCenter(costCenter);
            user.setManager(manager);
            user.setCreatedAt(LocalDateTime.now());
            return userRepository.save(user);
        });
    }
}

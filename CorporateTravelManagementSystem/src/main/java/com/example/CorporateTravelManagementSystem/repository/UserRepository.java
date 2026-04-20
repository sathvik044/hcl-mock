package com.example.CorporateTravelManagementSystem.Repository;

import com.example.CorporateTravelManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmployeeId(String employeeId);

    List<User> findByRoleIgnoreCase(String role);

    List<User> findByManagerId(Long managerId);
}

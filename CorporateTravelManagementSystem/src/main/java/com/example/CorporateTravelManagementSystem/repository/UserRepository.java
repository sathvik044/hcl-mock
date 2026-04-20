package com.example.CorporateTravelManagementSystem.repository;

import com.example.CorporateTravelManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
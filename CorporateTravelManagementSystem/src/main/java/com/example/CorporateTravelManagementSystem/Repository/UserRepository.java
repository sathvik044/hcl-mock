package com.example.CorporateTravelManagementSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateTravelManagementSystem.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

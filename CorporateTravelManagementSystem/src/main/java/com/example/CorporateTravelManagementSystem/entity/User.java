package com.example.CorporateTravelManagementSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.example.CorporateTravelManagementSystem.enums.UserRole;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String department;

    private String costCenter;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    private LocalDateTime createdAt;
}

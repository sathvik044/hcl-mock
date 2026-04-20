package com.example.CorporateTravelManagementSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "travel_budgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String costCenter;

    @Column(nullable = false)
    private String financialYear;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAllocated;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalUtilized;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingBalance;
}
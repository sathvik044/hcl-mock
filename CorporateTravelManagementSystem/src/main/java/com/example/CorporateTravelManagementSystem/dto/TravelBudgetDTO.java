package com.example.CorporateTravelManagementSystem.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelBudgetDTO {
    private Long id;
    private String department;
    private String costCenter;
    private String financialYear;
    private BigDecimal totalAllocated;
    private BigDecimal totalUtilized;
    private BigDecimal remainingBalance;
}
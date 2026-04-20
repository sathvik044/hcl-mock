package com.example.CorporateTravelManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetUtilizationResponseDto {

    private Long id;
    private String department;
    private String costCenter;
    private String financialYear;
    private double totalBudget;
    private double remainingBudget;
    private double utilizedBudget;
    private double utilizationPercentage;
}

package com.example.CorporateTravelManagementSystem.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetUtilizationDTO {
    private long budgetCount;
    private BigDecimal totalAllocated;
    private BigDecimal totalUtilized;
    private BigDecimal totalRemaining;
    private BigDecimal utilizationPercentage;
}

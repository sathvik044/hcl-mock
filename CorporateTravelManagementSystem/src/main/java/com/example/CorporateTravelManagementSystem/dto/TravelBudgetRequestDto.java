package com.example.CorporateTravelManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelBudgetRequestDTO {
    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Cost center is required")
    private String costCenter;

    @NotBlank(message = "Financial year is required")
    private String financialYear;

    @NotNull(message = "Total allocated is required")
    @Positive(message = "Total allocated must be greater than zero")
    private BigDecimal totalAllocated;
}

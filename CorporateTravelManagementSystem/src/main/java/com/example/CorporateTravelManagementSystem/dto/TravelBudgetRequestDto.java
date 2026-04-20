package com.example.CorporateTravelManagementSystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelBudgetRequestDto {

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Cost center is required")
    private String costCenter;

    @NotBlank(message = "Financial year is required")
    private String financialYear;

    @NotNull(message = "Total allocated is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total allocated must be greater than or equal to 0")
    private BigDecimal totalAllocated;

    @NotNull(message = "Total utilized is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total utilized must be greater than or equal to 0")
    private BigDecimal totalUtilized;
}
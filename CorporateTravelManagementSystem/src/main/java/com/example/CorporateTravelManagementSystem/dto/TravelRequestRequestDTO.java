package com.example.CorporateTravelManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelRequestRequestDTO {
    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Travel start date is required")
    private LocalDate travelStartDate;

    @NotNull(message = "Travel end date is required")
    private LocalDate travelEndDate;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    @NotNull(message = "Estimated cost is required")
    @Positive(message = "Estimated cost must be greater than zero")
    private BigDecimal estimatedCost;

    private String remarks;
}

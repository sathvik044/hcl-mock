package com.example.CorporateTravelManagementSystem.dto;

import com.example.CorporateTravelManagementSystem.enums.TravelStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelRequestDTO {
    private Long id;
    private UserDTO employee;
    private String destination;
    private LocalDate travelStartDate;
    private LocalDate travelEndDate;
    private String purpose;
    private TravelStatus status;
    private BigDecimal estimatedCost;
    private String remarks;
    private LocalDateTime submittedAt;
}
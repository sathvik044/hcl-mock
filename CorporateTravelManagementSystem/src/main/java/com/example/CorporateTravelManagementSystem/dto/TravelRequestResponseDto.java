package com.example.CorporateTravelManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.CorporateTravelManagementSystem.enums.TravelPurpose;
import com.example.CorporateTravelManagementSystem.enums.TravelStatus;
import com.example.CorporateTravelManagementSystem.enums.TravelType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelRequestResponseDto {

    private Long id;
    private String requestNumber;
    private Long employeeId;
    private String employeeName;
    private TravelType travelType;
    private TravelPurpose purpose;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String fromCity;
    private String toCity;
    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    private TravelStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

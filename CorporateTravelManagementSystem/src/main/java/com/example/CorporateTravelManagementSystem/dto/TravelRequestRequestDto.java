package com.example.CorporateTravelManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.CorporateTravelManagementSystem.enums.TravelPurpose;
import com.example.CorporateTravelManagementSystem.enums.TravelType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelRequestRequestDto {

    private Long employeeId;
    private TravelType travelType;
    private TravelPurpose purpose;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String fromCity;
    private String toCity;
    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
}

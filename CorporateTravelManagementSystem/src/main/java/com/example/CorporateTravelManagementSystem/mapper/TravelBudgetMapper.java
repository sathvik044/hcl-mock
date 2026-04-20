package com.example.CorporateTravelManagementSystem.mapper;

import com.example.CorporateTravelManagementSystem.dto.TravelBudgetRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelBudget;

import java.math.BigDecimal;

public class TravelBudgetMapper {

    private TravelBudgetMapper() {
    }

    public static TravelBudget toEntity(TravelBudgetRequestDto dto) {
        BigDecimal remainingBalance = dto.getTotalAllocated().subtract(dto.getTotalUtilized());

        return TravelBudget.builder()
                .department(dto.getDepartment())
                .costCenter(dto.getCostCenter())
                .financialYear(dto.getFinancialYear())
                .totalAllocated(dto.getTotalAllocated())
                .totalUtilized(dto.getTotalUtilized())
                .remainingBalance(remainingBalance)
                .build();
    }

    public static TravelBudgetResponseDto toResponseDto(TravelBudget entity) {
        return TravelBudgetResponseDto.builder()
                .id(entity.getId())
                .department(entity.getDepartment())
                .costCenter(entity.getCostCenter())
                .financialYear(entity.getFinancialYear())
                .totalAllocated(entity.getTotalAllocated())
                .totalUtilized(entity.getTotalUtilized())
                .remainingBalance(entity.getRemainingBalance())
                .build();
    }
}
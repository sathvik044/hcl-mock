package com.example.CorporateTravelManagementSystem.mapper;

import com.example.CorporateTravelManagementSystem.dto.TravelBudgetDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelBudget;
import org.springframework.stereotype.Component;

@Component
public class TravelBudgetMapper {

    public TravelBudgetDTO toDTO(TravelBudget travelBudget) {
        return TravelBudgetDTO.builder()
                .id(travelBudget.getId())
                .department(travelBudget.getDepartment())
                .costCenter(travelBudget.getCostCenter())
                .financialYear(travelBudget.getFinancialYear())
                .totalAllocated(travelBudget.getTotalAllocated())
                .totalUtilized(travelBudget.getTotalUtilized())
                .remainingBalance(travelBudget.getRemainingBalance())
                .build();
    }

    public TravelBudget toEntity(TravelBudgetRequestDTO requestDTO) {
        return TravelBudget.builder()
                .department(requestDTO.getDepartment())
                .costCenter(requestDTO.getCostCenter())
                .financialYear(requestDTO.getFinancialYear())
                .totalAllocated(requestDTO.getTotalAllocated())
                .build();
    }
}
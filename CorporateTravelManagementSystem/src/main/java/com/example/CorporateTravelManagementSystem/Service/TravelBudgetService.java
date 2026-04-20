package com.example.CorporateTravelManagementSystem.Service;

import java.util.List;

import com.example.CorporateTravelManagementSystem.dto.BudgetUtilizationResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelBudget;

public interface TravelBudgetService {

    List<TravelBudget> getBudgetsByDepartment(String department);

    List<TravelBudget> getBudgetsByCostCenter(String costCenter);

    List<BudgetUtilizationResponseDto> getBudgetUtilization();
}

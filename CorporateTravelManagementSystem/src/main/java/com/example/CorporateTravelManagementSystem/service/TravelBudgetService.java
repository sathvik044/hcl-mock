package com.example.CorporateTravelManagementSystem.Service;

import com.example.CorporateTravelManagementSystem.dto.BudgetUtilizationDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetRequestDTO;

import java.util.List;
import java.util.Optional;

public interface TravelBudgetService {
    List<TravelBudgetDTO> getAllTravelBudgets();

    Optional<TravelBudgetDTO> getTravelBudgetById(Long id);

    List<TravelBudgetDTO> getBudgetsByDepartment(String department);

    List<TravelBudgetDTO> getBudgetsByCostCenter(String costCenter);

    List<TravelBudgetDTO> getBudgetsByFinancialYear(String financialYear);

    Optional<TravelBudgetDTO> getBudgetByDepartmentAndYear(String department, String financialYear);

    BudgetUtilizationDTO getBudgetUtilization();

    TravelBudgetDTO createTravelBudget(TravelBudgetRequestDTO requestDTO);

    TravelBudgetDTO updateTravelBudget(Long id, TravelBudgetRequestDTO requestDTO);

    void deleteTravelBudget(Long id);

    TravelBudgetDTO updateUtilizedAmount(Long id, java.math.BigDecimal amount);
}

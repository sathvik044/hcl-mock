package com.example.CorporateTravelManagementSystem.service;

import com.example.CorporateTravelManagementSystem.dto.TravelBudgetRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetResponseDto;

import java.util.List;

public interface TravelBudgetService {

    TravelBudgetResponseDto createBudget(TravelBudgetRequestDto requestDto);

    TravelBudgetResponseDto updateBudget(Long id, TravelBudgetRequestDto requestDto);

    TravelBudgetResponseDto getBudgetById(Long id);

    List<TravelBudgetResponseDto> getAllBudgets();

    List<TravelBudgetResponseDto> getBudgetsByDepartment(String department);

    List<TravelBudgetResponseDto> getBudgetsByCostCenter(String costCenter);

    void deleteBudget(Long id);
}
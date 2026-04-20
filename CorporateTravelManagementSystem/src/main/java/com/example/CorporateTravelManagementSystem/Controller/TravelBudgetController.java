package com.example.CorporateTravelManagementSystem.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CorporateTravelManagementSystem.Service.TravelBudgetService;
import com.example.CorporateTravelManagementSystem.dto.BudgetUtilizationResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelBudget;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class TravelBudgetController {

    private final TravelBudgetService travelBudgetService;

    @GetMapping("/department/{department}")
    public List<TravelBudget> getBudgetsByDepartment(@PathVariable String department) {
        return travelBudgetService.getBudgetsByDepartment(department);
    }

    @GetMapping("/cost-center/{costCenter}")
    public List<TravelBudget> getBudgetsByCostCenter(@PathVariable String costCenter) {
        return travelBudgetService.getBudgetsByCostCenter(costCenter);
    }

    @GetMapping("/utilization")
    public List<BudgetUtilizationResponseDto> getBudgetUtilization() {
        return travelBudgetService.getBudgetUtilization();
    }
}

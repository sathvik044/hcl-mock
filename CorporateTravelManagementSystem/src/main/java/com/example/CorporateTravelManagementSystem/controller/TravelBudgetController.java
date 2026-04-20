package com.example.CorporateTravelManagementSystem.controller;

import com.example.CorporateTravelManagementSystem.dto.TravelBudgetRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetResponseDto;
import com.example.CorporateTravelManagementSystem.service.TravelBudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class TravelBudgetController {

    private final TravelBudgetService travelBudgetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TravelBudgetResponseDto createBudget(@Valid @RequestBody TravelBudgetRequestDto requestDto) {
        return travelBudgetService.createBudget(requestDto);
    }

    @PutMapping("/{id}")
    public TravelBudgetResponseDto updateBudget(@PathVariable Long id,
                                                @Valid @RequestBody TravelBudgetRequestDto requestDto) {
        return travelBudgetService.updateBudget(id, requestDto);
    }

    @GetMapping("/{id}")
    public TravelBudgetResponseDto getBudgetById(@PathVariable Long id) {
        return travelBudgetService.getBudgetById(id);
    }

    @GetMapping
    public List<TravelBudgetResponseDto> getAllBudgets() {
        return travelBudgetService.getAllBudgets();
    }

    @GetMapping("/department/{department}")
    public List<TravelBudgetResponseDto> getBudgetsByDepartment(@PathVariable String department) {
        return travelBudgetService.getBudgetsByDepartment(department);
    }

    @GetMapping("/cost-center/{costCenter}")
    public List<TravelBudgetResponseDto> getBudgetsByCostCenter(@PathVariable String costCenter) {
        return travelBudgetService.getBudgetsByCostCenter(costCenter);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudget(@PathVariable Long id) {
        travelBudgetService.deleteBudget(id);
    }
}
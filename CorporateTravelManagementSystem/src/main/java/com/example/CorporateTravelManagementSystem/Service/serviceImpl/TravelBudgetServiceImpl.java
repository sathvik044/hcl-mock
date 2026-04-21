package com.example.CorporateTravelManagementSystem.Service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.CorporateTravelManagementSystem.Repository.TravelBudgetRepository;
import com.example.CorporateTravelManagementSystem.Service.TravelBudgetService;
import com.example.CorporateTravelManagementSystem.dto.BudgetUtilizationResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelBudget;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelBudgetServiceImpl implements TravelBudgetService {

    private final TravelBudgetRepository travelBudgetRepository;

    @Override
    public TravelBudget createBudget(TravelBudget travelBudget) {
        travelBudget.setRemainingBudget(travelBudget.getTotalBudget());
        return travelBudgetRepository.save(travelBudget);
    }

    @Override
    public List<TravelBudget> getBudgetsByDepartment(String department) {
        return travelBudgetRepository.findByDepartmentIgnoreCase(department);
    }

    @Override
    public List<TravelBudget> getBudgetsByCostCenter(String costCenter) {
        return travelBudgetRepository.findByCostCenterIgnoreCase(costCenter);
    }

    @Override
    public List<BudgetUtilizationResponseDto> getBudgetUtilization() {
        return travelBudgetRepository.findAll()
                .stream()
                .map(this::mapToUtilizationResponse)
                .collect(Collectors.toList());
    }

    private BudgetUtilizationResponseDto mapToUtilizationResponse(TravelBudget travelBudget) {
        double utilizedBudget = travelBudget.getTotalBudget() - travelBudget.getRemainingBudget();
        double utilizationPercentage = 0;

        if (travelBudget.getTotalBudget() > 0) {
            utilizationPercentage = (utilizedBudget / travelBudget.getTotalBudget()) * 100;
        }

        return new BudgetUtilizationResponseDto(
                travelBudget.getId(),
                travelBudget.getDepartment(),
                travelBudget.getCostCenter(),
                travelBudget.getFinancialYear(),
                travelBudget.getTotalBudget(),
                travelBudget.getRemainingBudget(),
                utilizedBudget,
                utilizationPercentage);
    }
}

package com.example.CorporateTravelManagementSystem.service.serviceImpl;

import com.example.CorporateTravelManagementSystem.dto.TravelBudgetRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelBudget;
import com.example.CorporateTravelManagementSystem.exception.BadRequestException;
import com.example.CorporateTravelManagementSystem.exception.ResourceNotFoundException;
import com.example.CorporateTravelManagementSystem.mapper.TravelBudgetMapper;
import com.example.CorporateTravelManagementSystem.repository.TravelBudgetRepository;
import com.example.CorporateTravelManagementSystem.service.TravelBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelBudgetServiceImpl implements TravelBudgetService {

    private final TravelBudgetRepository travelBudgetRepository;

    @Override
    public TravelBudgetResponseDto createBudget(TravelBudgetRequestDto requestDto) {
        validateBudgetValues(requestDto);

        travelBudgetRepository
                .findByDepartmentIgnoreCaseAndCostCenterIgnoreCaseAndFinancialYear(
                        requestDto.getDepartment(),
                        requestDto.getCostCenter(),
                        requestDto.getFinancialYear()
                )
                .ifPresent(existing -> {
                    throw new BadRequestException("Budget already exists for this department, cost center, and financial year");
                });

        TravelBudget budget = TravelBudgetMapper.toEntity(requestDto);
        TravelBudget savedBudget = travelBudgetRepository.save(budget);
        return TravelBudgetMapper.toResponseDto(savedBudget);
    }

    @Override
    public TravelBudgetResponseDto updateBudget(Long id, TravelBudgetRequestDto requestDto) {
        validateBudgetValues(requestDto);

        TravelBudget existingBudget = travelBudgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelBudget not found with id: " + id));

        existingBudget.setDepartment(requestDto.getDepartment());
        existingBudget.setCostCenter(requestDto.getCostCenter());
        existingBudget.setFinancialYear(requestDto.getFinancialYear());
        existingBudget.setTotalAllocated(requestDto.getTotalAllocated());
        existingBudget.setTotalUtilized(requestDto.getTotalUtilized());
        existingBudget.setRemainingBalance(requestDto.getTotalAllocated().subtract(requestDto.getTotalUtilized()));

        TravelBudget updatedBudget = travelBudgetRepository.save(existingBudget);
        return TravelBudgetMapper.toResponseDto(updatedBudget);
    }

    @Override
    public TravelBudgetResponseDto getBudgetById(Long id) {
        TravelBudget budget = travelBudgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelBudget not found with id: " + id));
        return TravelBudgetMapper.toResponseDto(budget);
    }

    @Override
    public List<TravelBudgetResponseDto> getAllBudgets() {
        return travelBudgetRepository.findAll()
                .stream()
                .map(TravelBudgetMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<TravelBudgetResponseDto> getBudgetsByDepartment(String department) {
        return travelBudgetRepository.findByDepartmentIgnoreCase(department)
                .stream()
                .map(TravelBudgetMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<TravelBudgetResponseDto> getBudgetsByCostCenter(String costCenter) {
        return travelBudgetRepository.findByCostCenterIgnoreCase(costCenter)
                .stream()
                .map(TravelBudgetMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteBudget(Long id) {
        TravelBudget budget = travelBudgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelBudget not found with id: " + id));
        travelBudgetRepository.delete(budget);
    }

    private void validateBudgetValues(TravelBudgetRequestDto requestDto) {
        if (requestDto.getTotalUtilized().compareTo(requestDto.getTotalAllocated()) > 0) {
            throw new BadRequestException("Total utilized cannot be greater than total allocated");
        }

        BigDecimal remainingBalance = requestDto.getTotalAllocated().subtract(requestDto.getTotalUtilized());
        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Remaining balance cannot be negative");
        }
    }
}
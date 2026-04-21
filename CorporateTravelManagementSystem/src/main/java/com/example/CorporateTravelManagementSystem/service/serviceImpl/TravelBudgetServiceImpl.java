package com.example.CorporateTravelManagementSystem.Service.serviceImpl;

import com.example.CorporateTravelManagementSystem.dto.BudgetUtilizationDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelBudget;
import com.example.CorporateTravelManagementSystem.mapper.TravelBudgetMapper;
import com.example.CorporateTravelManagementSystem.Repository.TravelBudgetRepository;
import com.example.CorporateTravelManagementSystem.Service.TravelBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelBudgetServiceImpl implements TravelBudgetService {

    private final TravelBudgetRepository travelBudgetRepository;
    private final TravelBudgetMapper travelBudgetMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TravelBudgetDTO> getAllTravelBudgets() {
        return travelBudgetRepository.findAll().stream()
                .map(travelBudgetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TravelBudgetDTO> getTravelBudgetById(Long id) {
        return travelBudgetRepository.findById(id)
                .map(travelBudgetMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelBudgetDTO> getBudgetsByDepartment(String department) {
        return travelBudgetRepository.findByDepartmentIgnoreCase(department).stream()
                .map(travelBudgetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelBudgetDTO> getBudgetsByCostCenter(String costCenter) {
        return travelBudgetRepository.findByCostCenterIgnoreCase(costCenter).stream()
                .map(travelBudgetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelBudgetDTO> getBudgetsByFinancialYear(String financialYear) {
        return travelBudgetRepository.findByFinancialYear(financialYear).stream()
                .map(travelBudgetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TravelBudgetDTO> getBudgetByDepartmentAndYear(String department, String financialYear) {
        return travelBudgetRepository.findByDepartmentIgnoreCaseAndFinancialYear(department, financialYear)
                .map(travelBudgetMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetUtilizationDTO getBudgetUtilization() {
        List<TravelBudget> budgets = travelBudgetRepository.findAll();
        BigDecimal totalAllocated = budgets.stream()
                .map(TravelBudget::getTotalAllocated)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalUtilized = budgets.stream()
                .map(TravelBudget::getTotalUtilized)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalRemaining = budgets.stream()
                .map(TravelBudget::getRemainingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal utilizationPercentage = BigDecimal.ZERO;
        if (totalAllocated.compareTo(BigDecimal.ZERO) > 0) {
            utilizationPercentage = totalUtilized
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalAllocated, 2, RoundingMode.HALF_UP);
        }

        return BudgetUtilizationDTO.builder()
                .budgetCount(budgets.size())
                .totalAllocated(totalAllocated)
                .totalUtilized(totalUtilized)
                .totalRemaining(totalRemaining)
                .utilizationPercentage(utilizationPercentage)
                .build();
    }

    @Override
    public TravelBudgetDTO createTravelBudget(TravelBudgetRequestDTO requestDTO) {
        TravelBudget travelBudget = travelBudgetMapper.toEntity(requestDTO);
        travelBudget.setTotalUtilized(BigDecimal.ZERO);
        travelBudget.setRemainingBalance(requestDTO.getTotalAllocated());
        TravelBudget savedBudget = travelBudgetRepository.save(travelBudget);
        return travelBudgetMapper.toDTO(savedBudget);
    }

    @Override
    public TravelBudgetDTO updateTravelBudget(Long id, TravelBudgetRequestDTO requestDTO) {
        TravelBudget existingBudget = travelBudgetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Travel budget not found"));
        TravelBudget updatedBudget = travelBudgetMapper.toEntity(requestDTO);
        updatedBudget.setId(id);
        updatedBudget.setTotalUtilized(existingBudget.getTotalUtilized());
        updatedBudget.setRemainingBalance(requestDTO.getTotalAllocated().subtract(existingBudget.getTotalUtilized()));
        TravelBudget savedBudget = travelBudgetRepository.save(updatedBudget);
        return travelBudgetMapper.toDTO(savedBudget);
    }

    @Override
    public void deleteTravelBudget(Long id) {
        travelBudgetRepository.deleteById(id);
    }

    @Override
    public TravelBudgetDTO updateUtilizedAmount(Long id, BigDecimal amount) {
        TravelBudget travelBudget = travelBudgetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Travel budget not found"));
        travelBudget.setTotalUtilized(travelBudget.getTotalUtilized().add(amount));
        travelBudget.setRemainingBalance(travelBudget.getTotalAllocated().subtract(travelBudget.getTotalUtilized()));
        TravelBudget savedBudget = travelBudgetRepository.save(travelBudget);
        return travelBudgetMapper.toDTO(savedBudget);
    }
}

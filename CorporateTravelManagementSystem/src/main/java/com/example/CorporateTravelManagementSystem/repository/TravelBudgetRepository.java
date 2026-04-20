package com.example.CorporateTravelManagementSystem.repository;

import com.example.CorporateTravelManagementSystem.entity.TravelBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TravelBudgetRepository extends JpaRepository<TravelBudget, Long> {

    List<TravelBudget> findByDepartmentIgnoreCase(String department);

    List<TravelBudget> findByCostCenterIgnoreCase(String costCenter);

    Optional<TravelBudget> findByDepartmentIgnoreCaseAndCostCenterIgnoreCaseAndFinancialYear(
            String department,
            String costCenter,
            String financialYear
    );
}
package com.example.CorporateTravelManagementSystem.Repository;

import com.example.CorporateTravelManagementSystem.entity.TravelBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelBudgetRepository extends JpaRepository<TravelBudget, Long> {
    List<TravelBudget> findByDepartmentIgnoreCase(String department);

    List<TravelBudget> findByCostCenterIgnoreCase(String costCenter);

    List<TravelBudget> findByFinancialYear(String financialYear);

    Optional<TravelBudget> findByDepartmentIgnoreCaseAndFinancialYear(String department, String financialYear);
}

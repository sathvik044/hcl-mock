package com.example.CorporateTravelManagementSystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateTravelManagementSystem.entity.TravelBudget;

public interface TravelBudgetRepository extends JpaRepository<TravelBudget, Long> {

    List<TravelBudget> findByDepartmentIgnoreCase(String department);

    List<TravelBudget> findByCostCenterIgnoreCase(String costCenter);
}

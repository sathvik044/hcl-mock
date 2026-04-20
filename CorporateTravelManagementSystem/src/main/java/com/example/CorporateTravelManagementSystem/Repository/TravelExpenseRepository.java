package com.example.CorporateTravelManagementSystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateTravelManagementSystem.entity.TravelExpense;

public interface TravelExpenseRepository extends JpaRepository<TravelExpense, Long> {

    List<TravelExpense> findByTravelRequestId(Long travelRequestId);
}

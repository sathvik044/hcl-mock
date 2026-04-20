package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TravelExpense;

public interface TravelExpenseRepository extends JpaRepository<TravelExpense, Long> {

    List<TravelExpense> findByTravelRequestId(Long travelRequestId);
}
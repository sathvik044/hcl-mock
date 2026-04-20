package com.example.CorporateTravelManagementSystem.repository;

import com.example.CorporateTravelManagementSystem.entity.TravelRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {
}
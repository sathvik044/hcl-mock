package com.example.CorporateTravelManagementSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;

public interface TravelRequestRepository extends JpaRepository<TravelRequestEntity, Long> {
}

package com.example.CorporateTravelManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;

public interface TravelRequestRepository extends JpaRepository<TravelRequestEntity, Long> {

    List<TravelRequestEntity> findByEmployee_Id(Long employeeId);
}

package com.example.CorporateTravelManagementSystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;

import com.example.CorporateTravelManagementSystem.enums.TravelStatus;

public interface TravelRequestRepository extends JpaRepository<TravelRequestEntity, Long> {

    List<TravelRequestEntity> findByEmployee_Id(Long employeeId);

    List<TravelRequestEntity> findByStatus(TravelStatus status);
}

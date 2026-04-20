package com.example.CorporateTravelManagementSystem.repository;

import com.example.CorporateTravelManagementSystem.entity.TravelActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelActivityLogRepository extends JpaRepository<TravelActivityLog, Long> {

    List<TravelActivityLog> findByTravelRequestIdOrderByTimestampDesc(Long travelRequestId);

    List<TravelActivityLog> findByPerformedByIdOrderByTimestampDesc(Long performedByUserId);
}
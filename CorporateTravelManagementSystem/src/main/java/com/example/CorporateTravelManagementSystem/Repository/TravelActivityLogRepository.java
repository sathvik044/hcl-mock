package com.example.CorporateTravelManagementSystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateTravelManagementSystem.entity.TravelActivityLog;

public interface TravelActivityLogRepository extends JpaRepository<TravelActivityLog, Long> {

    List<TravelActivityLog> findByTravelRequestIdOrderByTimestampDesc(Long requestId);
}

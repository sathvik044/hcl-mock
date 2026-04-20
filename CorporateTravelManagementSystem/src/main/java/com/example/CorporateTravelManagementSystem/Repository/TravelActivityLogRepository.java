package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TravelActivityLog;

public interface TravelActivityLogRepository extends JpaRepository<TravelActivityLog, Long> {

    List<TravelActivityLog> findByTravelRequestIdOrderByTimestampDesc(Long requestId);
}
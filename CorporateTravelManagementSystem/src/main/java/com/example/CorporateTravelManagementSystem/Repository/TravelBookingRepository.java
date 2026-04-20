package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TravelBooking;

public interface TravelBookingRepository extends JpaRepository<TravelBooking, Long> {

    List<TravelBooking> findByTravelRequestId(Long travelRequestId);
}
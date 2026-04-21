package com.example.CorporateTravelManagementSystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateTravelManagementSystem.entity.Itinerary;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    List<Itinerary> findByTravelRequestId(Long travelRequestId);
}

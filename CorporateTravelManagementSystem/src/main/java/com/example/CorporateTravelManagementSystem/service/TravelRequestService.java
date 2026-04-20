package com.example.CorporateTravelManagementSystem.service;

import java.util.List;

import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestResponseDto;

public interface TravelRequestService {

    TravelRequestResponseDto createTravelRequest(TravelRequestRequestDto travelRequestRequestDto);

    TravelRequestResponseDto submitTravelRequest(Long id);

    TravelRequestResponseDto cancelTravelRequest(Long id);

    List<TravelRequestResponseDto> getAllTravelRequests();

    List<TravelRequestResponseDto> getTravelRequestsByEmployee(Long employeeId);
}

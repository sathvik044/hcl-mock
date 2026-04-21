package com.example.CorporateTravelManagementSystem.Service;

import java.util.List;

import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestResponseDto;

public interface TravelRequestService {

    TravelRequestResponseDto createTravelRequest(TravelRequestRequestDto travelRequestRequestDto);

    TravelRequestResponseDto submitTravelRequest(Long id);

    TravelRequestResponseDto cancelTravelRequest(Long id);

    List<TravelRequestResponseDto> getAllTravelRequests();

    List<TravelRequestResponseDto> getTravelRequestsByEmployee(Long employeeId);

    TravelRequestResponseDto approveByManager(Long id);

    TravelRequestResponseDto rejectByManager(Long id);

    TravelRequestResponseDto approveByFinance(Long id);

    TravelRequestResponseDto rejectByFinance(Long id);

    List<TravelRequestResponseDto> getBookedTravelRequests();

    TravelRequestResponseDto bookTravelRequest(Long id, com.example.CorporateTravelManagementSystem.dto.TravelBookingRequestDTO bookingRequest);

    TravelRequestResponseDto getTravelRequestById(Long id);
}

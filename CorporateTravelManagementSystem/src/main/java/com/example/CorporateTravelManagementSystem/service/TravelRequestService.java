package com.example.CorporateTravelManagementSystem.Service;

import com.example.CorporateTravelManagementSystem.dto.ApprovalActionRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDTO;
import com.example.CorporateTravelManagementSystem.enums.TravelStatus;

import java.util.List;
import java.util.Optional;

public interface TravelRequestService {
    List<TravelRequestDTO> getAllTravelRequests();

    Optional<TravelRequestDTO> getTravelRequestById(Long id);

    List<TravelRequestDTO> getTravelRequestsByEmployee(Long employeeId);

    List<TravelRequestDTO> getTravelRequestsByStatus(TravelStatus status);

    TravelRequestDTO createTravelRequest(TravelRequestRequestDTO requestDTO);

    TravelRequestDTO updateTravelRequest(Long id, TravelRequestRequestDTO requestDTO);

    TravelRequestDTO submitTravelRequest(Long id);

    TravelRequestDTO cancelTravelRequest(Long id);

    TravelRequestDTO managerApproveTravelRequest(Long id, ApprovalActionRequestDTO requestDTO);

    TravelRequestDTO managerRejectTravelRequest(Long id, ApprovalActionRequestDTO requestDTO);

    TravelRequestDTO financeApproveTravelRequest(Long id, ApprovalActionRequestDTO requestDTO);

    TravelRequestDTO financeRejectTravelRequest(Long id, ApprovalActionRequestDTO requestDTO);

    TravelRequestDTO updateTravelRequestStatus(Long id, TravelStatus status);

    void deleteTravelRequest(Long id);
}

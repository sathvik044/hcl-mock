package com.example.CorporateTravelManagementSystem.Service;

import com.example.CorporateTravelManagementSystem.dto.TravelApprovalDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelApprovalRequestDTO;

import java.util.List;
import java.util.Optional;

public interface TravelApprovalService {
    List<TravelApprovalDTO> getAllTravelApprovals();

    List<TravelApprovalDTO> getPendingManagerApprovals(Long managerId);

    List<TravelApprovalDTO> getPendingFinanceApprovals();

    Optional<TravelApprovalDTO> getTravelApprovalById(Long id);

    List<TravelApprovalDTO> getApprovalsByTravelRequest(Long travelRequestId);

    List<TravelApprovalDTO> getApprovalsByApprover(Long approverId);

    TravelApprovalDTO createTravelApproval(TravelApprovalRequestDTO requestDTO);

    TravelApprovalDTO updateTravelApproval(Long id, TravelApprovalRequestDTO requestDTO);

    TravelApprovalDTO approveTravelRequest(Long travelRequestId, Long approverId, String remarks);

    TravelApprovalDTO rejectTravelRequest(Long travelRequestId, Long approverId, String remarks);

    void deleteTravelApproval(Long id);
}

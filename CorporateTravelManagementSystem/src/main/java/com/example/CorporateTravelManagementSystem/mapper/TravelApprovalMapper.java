package com.example.CorporateTravelManagementSystem.mapper;

import com.example.CorporateTravelManagementSystem.dto.TravelApprovalDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelApprovalRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelApproval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TravelApprovalMapper {

    private final TravelRequestMapper travelRequestMapper;
    private final UserMapper userMapper;

    public TravelApprovalDTO toDTO(TravelApproval travelApproval) {
        return TravelApprovalDTO.builder()
                .id(travelApproval.getId())
                .travelRequest(travelRequestMapper.toDTO(travelApproval.getTravelRequest()))
                .approverType(travelApproval.getApproverType())
                .approver(userMapper.toDTO(travelApproval.getApprover()))
                .status(travelApproval.getStatus())
                .remarks(travelApproval.getRemarks())
                .approvedAt(travelApproval.getApprovedAt())
                .build();
    }

    public TravelApproval toEntity(TravelApprovalRequestDTO requestDTO) {
        return TravelApproval.builder()
                .approverType(requestDTO.getApproverType())
                .status(requestDTO.getStatus())
                .remarks(requestDTO.getRemarks())
                .build();
    }
}
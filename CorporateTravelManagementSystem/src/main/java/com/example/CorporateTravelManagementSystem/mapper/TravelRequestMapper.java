package com.example.CorporateTravelManagementSystem.mapper;

import com.example.CorporateTravelManagementSystem.dto.TravelRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TravelRequestMapper {

    private final UserMapper userMapper;

    public TravelRequestDTO toDTO(TravelRequest travelRequest) {
        return TravelRequestDTO.builder()
                .id(travelRequest.getId())
                .employee(userMapper.toDTO(travelRequest.getEmployee()))
                .destination(travelRequest.getDestination())
                .travelStartDate(travelRequest.getTravelStartDate())
                .travelEndDate(travelRequest.getTravelEndDate())
                .purpose(travelRequest.getPurpose())
                .status(travelRequest.getStatus())
                .estimatedCost(travelRequest.getEstimatedCost())
                .remarks(travelRequest.getRemarks())
                .submittedAt(travelRequest.getSubmittedAt())
                .build();
    }

    public TravelRequest toEntity(TravelRequestRequestDTO requestDTO) {
        return TravelRequest.builder()
                .destination(requestDTO.getDestination())
                .travelStartDate(requestDTO.getTravelStartDate())
                .travelEndDate(requestDTO.getTravelEndDate())
                .purpose(requestDTO.getPurpose())
                .estimatedCost(requestDTO.getEstimatedCost())
                .remarks(requestDTO.getRemarks())
                .build();
    }
}
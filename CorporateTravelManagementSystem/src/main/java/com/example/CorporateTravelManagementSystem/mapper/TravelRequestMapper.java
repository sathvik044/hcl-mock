package com.example.CorporateTravelManagementSystem.mapper;

import org.springframework.stereotype.Component;

import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;

@Component
public class TravelRequestMapper {

    public TravelRequestEntity toEntity(TravelRequestRequestDto travelRequestRequestDto) {
        TravelRequestEntity travelRequest = new TravelRequestEntity();
        travelRequest.setTravelType(travelRequestRequestDto.getTravelType());
        travelRequest.setPurpose(travelRequestRequestDto.getPurpose());
        travelRequest.setFromDate(travelRequestRequestDto.getFromDate());
        travelRequest.setToDate(travelRequestRequestDto.getToDate());
        travelRequest.setFromCity(travelRequestRequestDto.getFromCity());
        travelRequest.setToCity(travelRequestRequestDto.getToCity());
        travelRequest.setEstimatedCost(travelRequestRequestDto.getEstimatedCost());
        travelRequest.setActualCost(travelRequestRequestDto.getActualCost());
        return travelRequest;
    }

    public TravelRequestResponseDto toResponseDto(TravelRequestEntity travelRequest) {
        Long employeeId = null;
        String employeeName = null;

        if (travelRequest.getEmployee() != null) {
            employeeId = travelRequest.getEmployee().getId();
            employeeName = travelRequest.getEmployee().getName();
        }

        return new TravelRequestResponseDto(
                travelRequest.getId(),
                travelRequest.getRequestNumber(),
                employeeId,
                employeeName,
                travelRequest.getTravelType(),
                travelRequest.getPurpose(),
                travelRequest.getFromDate(),
                travelRequest.getToDate(),
                travelRequest.getFromCity(),
                travelRequest.getToCity(),
                travelRequest.getEstimatedCost(),
                travelRequest.getActualCost(),
                travelRequest.getStatus(),
                travelRequest.getCreatedAt(),
                travelRequest.getUpdatedAt());
    }
}

package com.example.CorporateTravelManagementSystem.mapper;

import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelActivityLog;

public class TravelActivityLogMapper {

    private TravelActivityLogMapper() {
    }

    public static TravelActivityLogResponseDto toResponseDto(TravelActivityLog entity) {
        return TravelActivityLogResponseDto.builder()
                .id(entity.getId())
                .travelRequestId(entity.getTravelRequest().getId())
                .requestNumber(entity.getTravelRequest().getRequestNumber())
                .action(entity.getAction())
                .performedByUserId(entity.getPerformedBy().getId())
                .performedByName(entity.getPerformedBy().getName())
                .oldStatus(entity.getOldStatus())
                .newStatus(entity.getNewStatus())
                .timestamp(entity.getTimestamp())
                .build();
    }
}
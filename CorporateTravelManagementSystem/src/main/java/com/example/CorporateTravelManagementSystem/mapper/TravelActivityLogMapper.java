package com.example.CorporateTravelManagementSystem.mapper;

import java.time.LocalDateTime;

import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogResponseDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelActivityLog;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.entity.User;

public class TravelActivityLogMapper {

    public static TravelActivityLog toEntity(TravelRequestEntity request,
                                             User user,
                                             String action,
                                             String oldStatus,
                                             String newStatus) {

        return TravelActivityLog.builder()
                .travelRequest(request)
                .performedBy(user)
                .action(action)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static TravelActivityLogResponseDTO toDTO(TravelActivityLog log) {

        return TravelActivityLogResponseDTO.builder()
                .id(log.getId())
                .travelRequestId(log.getTravelRequest().getId())
                .action(log.getAction())
                .performedBy(log.getPerformedBy().getId())
                .oldStatus(log.getOldStatus())
                .newStatus(log.getNewStatus())
                .timestamp(log.getTimestamp())
                .build();
    }
}

package com.example.CorporateTravelManagementSystem.dto;

import com.example.CorporateTravelManagementSystem.enums.ActivityActionType;
import com.example.CorporateTravelManagementSystem.enums.TravelRequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelActivityLogResponseDto {

    private Long id;
    private Long travelRequestId;
    private String requestNumber;
    private ActivityActionType action;
    private Long performedByUserId;
    private String performedByName;
    private TravelRequestStatus oldStatus;
    private TravelRequestStatus newStatus;
    private LocalDateTime timestamp;
}
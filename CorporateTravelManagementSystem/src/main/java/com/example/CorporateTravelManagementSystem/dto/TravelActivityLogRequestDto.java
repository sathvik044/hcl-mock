package com.example.CorporateTravelManagementSystem.dto;

import com.example.CorporateTravelManagementSystem.enums.ActivityActionType;
import com.example.CorporateTravelManagementSystem.enums.TravelRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelActivityLogRequestDto {

    @NotNull(message = "Travel request id is required")
    private Long travelRequestId;

    @NotNull(message = "Action is required")
    private ActivityActionType action;

    @NotNull(message = "Performed by user id is required")
    private Long performedByUserId;

    private TravelRequestStatus oldStatus;
    private TravelRequestStatus newStatus;
}
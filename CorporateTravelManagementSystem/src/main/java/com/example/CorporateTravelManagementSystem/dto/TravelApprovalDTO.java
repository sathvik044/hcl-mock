package com.example.CorporateTravelManagementSystem.dto;

import com.example.CorporateTravelManagementSystem.enums.ApprovalStatus;
import com.example.CorporateTravelManagementSystem.enums.ApproverType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelApprovalDTO {
    private Long id;
    private TravelRequestDTO travelRequest;
    private ApproverType approverType;
    private UserDTO approver;
    private ApprovalStatus status;
    private String remarks;
    private LocalDateTime approvedAt;
}
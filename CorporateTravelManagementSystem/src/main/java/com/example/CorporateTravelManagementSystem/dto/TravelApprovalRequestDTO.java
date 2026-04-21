package com.example.CorporateTravelManagementSystem.dto;

import com.example.CorporateTravelManagementSystem.enums.ApprovalStatus;
import com.example.CorporateTravelManagementSystem.enums.ApproverType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelApprovalRequestDTO {
    @NotNull(message = "Travel request ID is required")
    private Long travelRequestId;

    @NotNull(message = "Approver type is required")
    private ApproverType approverType;

    @NotNull(message = "Approver ID is required")
    private Long approverId;

    private ApprovalStatus status;
    private String remarks;
}

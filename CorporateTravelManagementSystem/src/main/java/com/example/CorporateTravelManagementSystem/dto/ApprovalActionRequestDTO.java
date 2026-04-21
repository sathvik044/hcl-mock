package com.example.CorporateTravelManagementSystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalActionRequestDTO {
    @NotNull(message = "Approver ID is required")
    private Long approverId;

    private String remarks;
}

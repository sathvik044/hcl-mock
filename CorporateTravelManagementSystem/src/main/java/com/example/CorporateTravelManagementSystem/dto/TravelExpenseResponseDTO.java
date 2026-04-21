package com.example.CorporateTravelManagementSystem.dto;

import com.example.CorporateTravelManagementSystem.enums.ExpenseStatus;
import com.example.CorporateTravelManagementSystem.enums.ExpenseType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelExpenseResponseDTO {

    private Long id;
    private Long travelRequestId;
    private ExpenseType expenseType;
    private Double amount;
    private String receiptUrl;

    private Long claimedBy;
    private ExpenseStatus status;

    private Long verifiedBy;
    private String remarks;
}

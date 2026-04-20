package com.example.CorporateTravelManagementSystem.dto;

import com.example.CorporateTravelManagementSystem.enums.ExpenseType;

import lombok.Data;

@Data
public class TravelExpenseRequestDTO {

    private Long travelRequestId;
    private ExpenseType expenseType;
    private Double amount;
    private String receiptUrl;
    private Long claimedBy;
}

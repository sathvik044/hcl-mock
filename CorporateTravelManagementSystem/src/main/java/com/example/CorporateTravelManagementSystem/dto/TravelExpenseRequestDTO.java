package com.example.demo.dto;

import com.example.demo.enums.ExpenseType;

import lombok.Data;

@Data
public class TravelExpenseRequestDTO {

    private Long travelRequestId;
    private ExpenseType expenseType;
    private Double amount;
    private String receiptUrl;
    private Long claimedBy;
}
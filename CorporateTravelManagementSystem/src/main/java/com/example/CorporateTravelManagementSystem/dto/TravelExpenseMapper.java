package com.example.demo.mapper;

import com.example.demo.dto.TravelExpenseRequestDTO;
import com.example.demo.dto.TravelExpenseResponseDTO;
import com.example.demo.entity.TravelExpense;
import com.example.demo.entity.TravelRequest;
import com.example.demo.entity.User;
import com.example.demo.enums.ExpenseStatus;

public class TravelExpenseMapper {

    public static TravelExpense toEntity(TravelExpenseRequestDTO dto,
                                         TravelRequest request,
                                         User user) {

        return TravelExpense.builder()
                .travelRequest(request)
                .expenseType(dto.getExpenseType())
                .amount(dto.getAmount())
                .receiptUrl(dto.getReceiptUrl())
                .claimedBy(user)
                .status(ExpenseStatus.PENDING)
                .build();
    }

    public static TravelExpenseResponseDTO toDTO(TravelExpense e) {

        return TravelExpenseResponseDTO.builder()
                .id(e.getId())
                .travelRequestId(e.getTravelRequest().getId())
                .expenseType(e.getExpenseType())
                .amount(e.getAmount())
                .receiptUrl(e.getReceiptUrl())
                .claimedBy(e.getClaimedBy().getId())
                .status(e.getStatus())
                .verifiedBy(e.getVerifiedBy() != null ? e.getVerifiedBy().getId() : null)
                .remarks(e.getRemarks())
                .build();
    }
}
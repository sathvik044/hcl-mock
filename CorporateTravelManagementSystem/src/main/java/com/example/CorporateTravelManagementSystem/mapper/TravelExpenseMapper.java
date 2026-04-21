package com.example.CorporateTravelManagementSystem.mapper;

import com.example.CorporateTravelManagementSystem.dto.TravelExpenseRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelExpenseResponseDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelExpense;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.ExpenseStatus;

public class TravelExpenseMapper {

    public static TravelExpense toEntity(TravelExpenseRequestDTO dto,
                                         TravelRequestEntity request,
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

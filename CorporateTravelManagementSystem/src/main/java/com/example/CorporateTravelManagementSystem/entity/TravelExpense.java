package com.example.CorporateTravelManagementSystem.entity;

import com.example.CorporateTravelManagementSystem.enums.ExpenseStatus;
import com.example.CorporateTravelManagementSystem.enums.ExpenseType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "travel_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    @ManyToOne
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequestEntity travelRequest;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    private Double amount;

    private String receiptUrl;

    
    @ManyToOne
    @JoinColumn(name = "claimed_by", nullable = false)
    private User claimedBy;

    @Enumerated(EnumType.STRING)
    private ExpenseStatus status;

    
    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    private String remarks;
}

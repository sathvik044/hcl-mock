package com.example.CorporateTravelManagementSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.time.LocalDateTime;
import com.example.CorporateTravelManagementSystem.enums.ApprovalStatus;    

@Entity
@Data
public class TravelApproval {
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "travel_request_id")
    private Itinerary travelRequest;
    @ManyToOne
    @JoinColumn(name = "approver_id")   
    private User approver;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;
    private String comments;
    private LocalDateTime createdAt;
    
}

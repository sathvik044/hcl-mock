package com.example.CorporateTravelManagementSystem.entity;

import com.example.CorporateTravelManagementSystem.enums.ApprovalStatus;
import com.example.CorporateTravelManagementSystem.enums.ApproverType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "travel_approvals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApproverType approverType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status;

    private String remarks;

    private LocalDateTime approvedAt;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = ApprovalStatus.PENDING;
        }
    }
}
package com.example.CorporateTravelManagementSystem.entity;

import com.example.CorporateTravelManagementSystem.enums.TravelStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "travel_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDate travelStartDate;

    @Column(nullable = false)
    private LocalDate travelEndDate;

    @Column(nullable = false)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TravelStatus status;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal estimatedCost;

    private String remarks;

    private LocalDateTime submittedAt;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = TravelStatus.DRAFT;
        }
        if (status != TravelStatus.DRAFT && submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
    }
}

package com.example.CorporateTravelManagementSystem.entity;

import com.example.CorporateTravelManagementSystem.enums.ActivityActionType;
import com.example.CorporateTravelManagementSystem.enums.TravelRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "travel_activity_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityActionType action;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private TravelRequestStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private TravelRequestStatus newStatus;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
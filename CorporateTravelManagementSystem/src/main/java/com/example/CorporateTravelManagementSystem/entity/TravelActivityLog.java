package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    private String action; 
    @ManyToOne
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;

    private String oldStatus;
    private String newStatus;

    private LocalDateTime timestamp;
}
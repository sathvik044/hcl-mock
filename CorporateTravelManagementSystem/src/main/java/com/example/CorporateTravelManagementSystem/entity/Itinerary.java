package com.example.CorporateTravelManagementSystem.entity;

import java.time.LocalDateTime;

import com.example.CorporateTravelManagementSystem.enums.ItineraryStatus;
import com.example.CorporateTravelManagementSystem.enums.SegmentType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itineraries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequestEntity travelRequest;

    @Enumerated(EnumType.STRING)
    private SegmentType segmentType;

    private String providerName;
    private String bookingReference;

    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;

    private String fromLocation;
    private String toLocation;

    private Double cost;

    @Enumerated(EnumType.STRING)
    private ItineraryStatus status;
}

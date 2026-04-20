package com.example.CorporateTravelManagementSystem.dto;

import java.time.LocalDateTime;

import com.example.CorporateTravelManagementSystem.enums.SegmentType;

import lombok.Data;

@Data
public class ItineraryRequestDTO {

    private Long travelRequestId;
    private SegmentType segmentType;
    private String providerName;
    private String bookingReference;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private String fromLocation;
    private String toLocation;
    private Double cost;
}

package com.example.demo.dto;

import java.time.LocalDateTime;
import com.example.demo.enums.ItineraryStatus;
import com.example.demo.enums.SegmentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItineraryResponseDTO {

    private Long id;
    private Long travelRequestId;
    private SegmentType segmentType;
    private String providerName;
    private String bookingReference;

    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;

    private String fromLocation;
    private String toLocation;

    private Double cost;
    private ItineraryStatus status;
}
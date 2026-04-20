package com.example.demo.mapper;

import com.example.demo.dto.ItineraryRequestDTO;
import com.example.demo.dto.ItineraryResponseDTO;
import com.example.demo.entity.Itinerary;
import com.example.demo.entity.TravelRequest;
import com.example.demo.enums.ItineraryStatus;

public class ItineraryMapper {

    public static Itinerary toEntity(ItineraryRequestDTO dto, TravelRequest travelRequest) {
        return Itinerary.builder()
                .travelRequest(travelRequest)
                .segmentType(dto.getSegmentType())
                .providerName(dto.getProviderName())
                .bookingReference(dto.getBookingReference())
                .departureDateTime(dto.getDepartureDateTime())
                .arrivalDateTime(dto.getArrivalDateTime())
                .fromLocation(dto.getFromLocation())
                .toLocation(dto.getToLocation())
                .cost(dto.getCost())
                .status(ItineraryStatus.PENDING)
                .build();
    }
    public static ItineraryResponseDTO toDTO(Itinerary entity) {
        return ItineraryResponseDTO.builder()
                .id(entity.getId())
                .travelRequestId(entity.getTravelRequest().getId())
                .segmentType(entity.getSegmentType())
                .providerName(entity.getProviderName())
                .bookingReference(entity.getBookingReference())
                .departureDateTime(entity.getDepartureDateTime())
                .arrivalDateTime(entity.getArrivalDateTime())
                .fromLocation(entity.getFromLocation())
                .toLocation(entity.getToLocation())
                .cost(entity.getCost())
                .status(entity.getStatus())
                .build();
    }
}
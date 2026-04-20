package com.example.CorporateTravelManagementSystem.mapper;

import com.example.CorporateTravelManagementSystem.dto.ItineraryRequestDto;
import com.example.CorporateTravelManagementSystem.dto.ItineraryResponseDto;
import com.example.CorporateTravelManagementSystem.entity.Itinerary;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.enums.ItineraryStatus;

public class ItineraryMapper {

    public static Itinerary toEntity(ItineraryRequestDto dto, TravelRequestEntity travelRequest) {
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
    public static ItineraryResponseDto toDTO(Itinerary entity) {
        return ItineraryResponseDto.builder()
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

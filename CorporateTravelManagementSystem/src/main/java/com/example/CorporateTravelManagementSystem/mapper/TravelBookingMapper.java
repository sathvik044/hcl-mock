package com.example.CorporateTravelManagementSystem.mapper;

import java.time.LocalDate;

import com.example.CorporateTravelManagementSystem.dto.TravelBookingRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBookingResponseDTO;
import com.example.CorporateTravelManagementSystem.entity.Itinerary;
import com.example.CorporateTravelManagementSystem.entity.TravelBooking;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.PaymentStatus;

public class TravelBookingMapper {

    public static TravelBooking toEntity(TravelBookingRequestDTO dto,
                                         TravelRequestEntity travelRequest,
                                         Itinerary itinerary,
                                         User user) {

        return TravelBooking.builder()
                .travelRequest(travelRequest)
                .itinerary(itinerary)
                .bookedBy(user)
                .bookingDate(LocalDate.now())
                .paymentStatus(PaymentStatus.PAID) 
                .invoiceUrl(dto.getInvoiceUrl())
                .build();
    }
    public static TravelBookingResponseDTO toDTO(TravelBooking entity) {

        return TravelBookingResponseDTO.builder()
                .id(entity.getId())
                .travelRequestId(entity.getTravelRequest().getId())
                .itineraryId(entity.getItinerary().getId())
                .bookedBy(entity.getBookedBy().getId())
                .bookingDate(entity.getBookingDate())
                .paymentStatus(entity.getPaymentStatus())
                .invoiceUrl(entity.getInvoiceUrl())
                .build();
    }
}

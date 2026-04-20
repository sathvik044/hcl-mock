package com.example.demo.mapper;

import java.time.LocalDate;

import com.example.demo.dto.TravelBookingRequestDTO;
import com.example.demo.dto.TravelBookingResponseDTO;
import com.example.demo.entity.Itinerary;
import com.example.demo.entity.TravelBooking;
import com.example.demo.entity.TravelRequest;
import com.example.demo.entity.User;
import com.example.demo.enums.PaymentStatus;

public class TravelBookingMapper {

    public static TravelBooking toEntity(TravelBookingRequestDTO dto,
                                         TravelRequest travelRequest,
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
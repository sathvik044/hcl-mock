package com.example.CorporateTravelManagementSystem.dto;

import java.time.LocalDate;

import com.example.CorporateTravelManagementSystem.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelBookingResponseDTO {

    private Long id;
    private Long travelRequestId;
    private Long itineraryId;
    private Long bookedBy;
    private LocalDate bookingDate;
    private PaymentStatus paymentStatus;
    private String invoiceUrl;
}

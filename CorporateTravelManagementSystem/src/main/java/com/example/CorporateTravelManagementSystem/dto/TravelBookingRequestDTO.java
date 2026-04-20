package com.example.demo.dto;

import lombok.Data;

@Data
public class TravelBookingRequestDTO {

    private Long travelRequestId;
    private Long itineraryId;
    private Long bookedBy; 
    private String invoiceUrl;
}
package com.example.demo.entity;

import java.time.LocalDate;

import com.example.demo.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "travel_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @ManyToOne
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @ManyToOne
    @JoinColumn(name = "booked_by", nullable = false)
    private User bookedBy;

    private LocalDate bookingDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String invoiceUrl;
}
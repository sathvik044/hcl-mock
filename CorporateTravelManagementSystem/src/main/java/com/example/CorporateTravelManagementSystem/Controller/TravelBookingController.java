package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TravelBookingRequestDTO;
import com.example.demo.dto.TravelBookingResponseDTO;
import com.example.demo.service.TravelBookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class TravelBookingController {

    private final TravelBookingService bookingService;

    @PostMapping
    public TravelBookingResponseDTO create(@RequestBody TravelBookingRequestDTO dto) {
        return bookingService.create(dto);
    }

    @GetMapping("/travel-request/{id}")
    public List<TravelBookingResponseDTO> getByRequest(@PathVariable Long id) {
        return bookingService.getByRequest(id);
    }
}
package com.example.CorporateTravelManagementSystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestResponseDto;
import com.example.CorporateTravelManagementSystem.service.TravelRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/travel-requests")
@RequiredArgsConstructor
public class TravelRequestController {

    private final TravelRequestService travelRequestService;

    @PostMapping
    public TravelRequestResponseDto createTravelRequest(@RequestBody TravelRequestRequestDto travelRequestRequestDto) {
        return travelRequestService.createTravelRequest(travelRequestRequestDto);
    }

    @PutMapping("/{id}/submit")
    public TravelRequestResponseDto submitTravelRequest(@PathVariable Long id) {
        return travelRequestService.submitTravelRequest(id);
    }

    @PutMapping("/{id}/cancel")
    public TravelRequestResponseDto cancelTravelRequest(@PathVariable Long id) {
        return travelRequestService.cancelTravelRequest(id);
    }

    @GetMapping
    public List<TravelRequestResponseDto> getAllTravelRequests() {
        return travelRequestService.getAllTravelRequests();
    }

    @GetMapping("/employee/{employeeId}")
    public List<TravelRequestResponseDto> getTravelRequestsByEmployee(@PathVariable Long employeeId) {
        return travelRequestService.getTravelRequestsByEmployee(employeeId);
    }
}

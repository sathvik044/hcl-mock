package com.example.CorporateTravelManagementSystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.CorporateTravelManagementSystem.Service.ItineraryService;
import com.example.CorporateTravelManagementSystem.dto.ItineraryRequestDto;
import com.example.CorporateTravelManagementSystem.dto.ItineraryResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;

    @PostMapping
    public ItineraryResponseDto create(@RequestBody ItineraryRequestDto dto) {
        return itineraryService.create(dto);
    }

    @GetMapping("/travel-request/{id}")
    public List<ItineraryResponseDto> getByTravelRequest(@PathVariable Long id) {
        return itineraryService.getByTravelRequest(id);
    }

    @PutMapping("/{id}/confirm")
    public ItineraryResponseDto confirm(@PathVariable Long id) {
        return itineraryService.confirm(id);
    }
}

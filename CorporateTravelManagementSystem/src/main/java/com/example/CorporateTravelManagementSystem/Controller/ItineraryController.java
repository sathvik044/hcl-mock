package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ItineraryRequestDTO;
import com.example.demo.dto.ItineraryResponseDTO;
import com.example.demo.service.ItineraryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;

    @PostMapping
    public ItineraryResponseDTO create(@RequestBody ItineraryRequestDTO dto) {
        return itineraryService.create(dto);
    }

    @GetMapping("/travel-request/{id}")
    public List<ItineraryResponseDTO> getByTravelRequest(@PathVariable Long id) {
        return itineraryService.getByTravelRequest(id);
    }

    @PutMapping("/{id}/confirm")
    public ItineraryResponseDTO confirm(@PathVariable Long id) {
        return itineraryService.confirm(id);
    }
}
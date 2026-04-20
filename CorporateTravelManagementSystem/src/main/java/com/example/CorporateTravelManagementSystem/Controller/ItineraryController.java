package com.example.CorporateTravelManagementSystem.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.CorporateTravelManagementSystem.Service.ItineraryService;
import com.example.CorporateTravelManagementSystem.dto.ItineraryRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.ItineraryResponseDTO;

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

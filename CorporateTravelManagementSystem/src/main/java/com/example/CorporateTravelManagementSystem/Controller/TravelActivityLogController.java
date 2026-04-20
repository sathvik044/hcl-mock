package com.example.CorporateTravelManagementSystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CorporateTravelManagementSystem.Service.TravelActivityLogService;
import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class TravelActivityLogController {

    private final TravelActivityLogService logService;

    @GetMapping("/travel-request/{id}")
    public List<TravelActivityLogResponseDTO> getLogs(@PathVariable Long id) {
        return logService.getLogs(id);
    }
}

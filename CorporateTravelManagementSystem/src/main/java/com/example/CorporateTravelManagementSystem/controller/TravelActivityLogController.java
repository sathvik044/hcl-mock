package com.example.CorporateTravelManagementSystem.controller;

import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogResponseDto;
import com.example.CorporateTravelManagementSystem.service.TravelActivityLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class TravelActivityLogController {

    private final TravelActivityLogService travelActivityLogService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TravelActivityLogResponseDto createLog(@Valid @RequestBody TravelActivityLogRequestDto requestDto) {
        return travelActivityLogService.createLog(requestDto);
    }

    @GetMapping("/{id}")
    public TravelActivityLogResponseDto getLogById(@PathVariable Long id) {
        return travelActivityLogService.getLogById(id);
    }

    @GetMapping
    public List<TravelActivityLogResponseDto> getAllLogs() {
        return travelActivityLogService.getAllLogs();
    }

    @GetMapping("/travel-request/{travelRequestId}")
    public List<TravelActivityLogResponseDto> getLogsByTravelRequestId(@PathVariable Long travelRequestId) {
        return travelActivityLogService.getLogsByTravelRequestId(travelRequestId);
    }

    @GetMapping("/performed-by/{userId}")
    public List<TravelActivityLogResponseDto> getLogsByPerformedByUserId(@PathVariable Long userId) {
        return travelActivityLogService.getLogsByPerformedByUserId(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLog(@PathVariable Long id) {
        travelActivityLogService.deleteLog(id);
    }
}
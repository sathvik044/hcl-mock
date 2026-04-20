package com.example.CorporateTravelManagementSystem.service;

import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogResponseDto;

import java.util.List;

public interface TravelActivityLogService {

    TravelActivityLogResponseDto createLog(TravelActivityLogRequestDto requestDto);

    TravelActivityLogResponseDto getLogById(Long id);

    List<TravelActivityLogResponseDto> getAllLogs();

    List<TravelActivityLogResponseDto> getLogsByTravelRequestId(Long travelRequestId);

    List<TravelActivityLogResponseDto> getLogsByPerformedByUserId(Long performedByUserId);

    void deleteLog(Long id);
}
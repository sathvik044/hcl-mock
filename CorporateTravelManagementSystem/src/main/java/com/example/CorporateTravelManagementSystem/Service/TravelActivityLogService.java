package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.TravelActivityLogResponseDTO;
import com.example.demo.entity.TravelActivityLog;
import com.example.demo.entity.TravelRequest;
import com.example.demo.entity.User;
import com.example.demo.mapper.TravelActivityLogMapper;
import com.example.demo.repository.TravelActivityLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelActivityLogService {

    private final TravelActivityLogRepository logRepository;

    public void logAction(TravelRequest request,
                          User user,
                          String action,
                          String oldStatus,
                          String newStatus) {

        TravelActivityLog log = TravelActivityLogMapper
                .toEntity(request, user, action, oldStatus, newStatus);

        logRepository.save(log);
    }

    public List<TravelActivityLogResponseDTO> getLogs(Long requestId) {

        return logRepository.findByTravelRequestIdOrderByTimestampDesc(requestId)
                .stream()
                .map(TravelActivityLogMapper::toDTO)
                .collect(Collectors.toList());
    }
}
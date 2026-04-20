package com.example.CorporateTravelManagementSystem.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.CorporateTravelManagementSystem.Repository.TravelActivityLogRepository;
import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogResponseDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelActivityLog;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.mapper.TravelActivityLogMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelActivityLogService {

    private final TravelActivityLogRepository logRepository;

    public void logAction(TravelRequestEntity request,
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

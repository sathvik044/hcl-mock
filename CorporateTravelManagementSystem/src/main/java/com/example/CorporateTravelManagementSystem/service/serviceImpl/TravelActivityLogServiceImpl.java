package com.example.CorporateTravelManagementSystem.service.serviceImpl;

import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelActivityLogResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelActivityLog;
import com.example.CorporateTravelManagementSystem.entity.TravelRequest;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.exception.ResourceNotFoundException;
import com.example.CorporateTravelManagementSystem.mapper.TravelActivityLogMapper;
import com.example.CorporateTravelManagementSystem.repository.TravelActivityLogRepository;
import com.example.CorporateTravelManagementSystem.repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.repository.UserRepository;
import com.example.CorporateTravelManagementSystem.service.TravelActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelActivityLogServiceImpl implements TravelActivityLogService {

    private final TravelActivityLogRepository travelActivityLogRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final UserRepository userRepository;

    @Override
    public TravelActivityLogResponseDto createLog(TravelActivityLogRequestDto requestDto) {
        TravelRequest travelRequest = travelRequestRepository.findById(requestDto.getTravelRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("TravelRequest not found with id: " + requestDto.getTravelRequestId()));

        User performedBy = userRepository.findById(requestDto.getPerformedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDto.getPerformedByUserId()));

        TravelActivityLog log = TravelActivityLog.builder()
                .travelRequest(travelRequest)
                .action(requestDto.getAction())
                .performedBy(performedBy)
                .oldStatus(requestDto.getOldStatus())
                .newStatus(requestDto.getNewStatus())
                .build();

        TravelActivityLog savedLog = travelActivityLogRepository.save(log);
        return TravelActivityLogMapper.toResponseDto(savedLog);
    }

    @Override
    public TravelActivityLogResponseDto getLogById(Long id) {
        TravelActivityLog log = travelActivityLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelActivityLog not found with id: " + id));
        return TravelActivityLogMapper.toResponseDto(log);
    }

    @Override
    public List<TravelActivityLogResponseDto> getAllLogs() {
        return travelActivityLogRepository.findAll()
                .stream()
                .map(TravelActivityLogMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<TravelActivityLogResponseDto> getLogsByTravelRequestId(Long travelRequestId) {
        return travelActivityLogRepository.findByTravelRequestIdOrderByTimestampDesc(travelRequestId)
                .stream()
                .map(TravelActivityLogMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<TravelActivityLogResponseDto> getLogsByPerformedByUserId(Long performedByUserId) {
        return travelActivityLogRepository.findByPerformedByIdOrderByTimestampDesc(performedByUserId)
                .stream()
                .map(TravelActivityLogMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteLog(Long id) {
        TravelActivityLog log = travelActivityLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TravelActivityLog not found with id: " + id));
        travelActivityLogRepository.delete(log);
    }
}
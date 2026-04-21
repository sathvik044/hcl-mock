package com.example.CorporateTravelManagementSystem.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.CorporateTravelManagementSystem.Exception.TravelRequestNotFoundException;
import com.example.CorporateTravelManagementSystem.Exception.TravelRequestStateException;
import com.example.CorporateTravelManagementSystem.Exception.UserNotFoundException;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.TravelStatus;
import com.example.CorporateTravelManagementSystem.mapper.TravelRequestMapper;
import com.example.CorporateTravelManagementSystem.repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.repository.UserRepository;
import com.example.CorporateTravelManagementSystem.service.TravelRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelRequestServiceImpl implements TravelRequestService {

    private final TravelRequestRepository travelRequestRepository;
    private final UserRepository userRepository;
    private final TravelRequestMapper travelRequestMapper;

    @Override
    public TravelRequestResponseDto createTravelRequest(TravelRequestRequestDto travelRequestRequestDto) {
        Long employeeId = travelRequestRequestDto.getEmployeeId();

        if (employeeId == null) {
            throw new UserNotFoundException("Employee id is required");
        }

        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + employeeId));

        TravelRequestEntity travelRequest = travelRequestMapper.toEntity(travelRequestRequestDto);
        travelRequest.setEmployee(employee);
        travelRequest.setRequestNumber(generateRequestNumber());
        travelRequest.setStatus(TravelStatus.DRAFT);
        travelRequest.setCreatedAt(LocalDateTime.now());
        travelRequest.setUpdatedAt(LocalDateTime.now());

        TravelRequestEntity savedTravelRequest = travelRequestRepository.save(travelRequest);
        return travelRequestMapper.toResponseDto(savedTravelRequest);
    }

    @Override
    public TravelRequestResponseDto submitTravelRequest(Long id) {
        TravelRequestEntity travelRequest = getTravelRequestById(id);

        if (travelRequest.getStatus() != TravelStatus.DRAFT) {
            throw new TravelRequestStateException("Only draft travel requests can be submitted");
        }

        travelRequest.setStatus(TravelStatus.SUBMITTED);
        travelRequest.setUpdatedAt(LocalDateTime.now());
        TravelRequestEntity savedTravelRequest = travelRequestRepository.save(travelRequest);
        return travelRequestMapper.toResponseDto(savedTravelRequest);
    }

    @Override
    public TravelRequestResponseDto cancelTravelRequest(Long id) {
        TravelRequestEntity travelRequest = getTravelRequestById(id);

        if (travelRequest.getStatus() == TravelStatus.CANCELLED) {
            throw new TravelRequestStateException("Travel request is already cancelled");
        }

        if (travelRequest.getStatus() == TravelStatus.COMPLETED) {
            throw new TravelRequestStateException("Completed travel requests cannot be cancelled");
        }

        travelRequest.setStatus(TravelStatus.CANCELLED);
        travelRequest.setUpdatedAt(LocalDateTime.now());
        TravelRequestEntity savedTravelRequest = travelRequestRepository.save(travelRequest);
        return travelRequestMapper.toResponseDto(savedTravelRequest);
    }

    @Override
    public List<TravelRequestResponseDto> getAllTravelRequests() {
        return travelRequestRepository.findAll()
                .stream()
                .map(travelRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TravelRequestResponseDto> getTravelRequestsByEmployee(Long employeeId) {
        if (!userRepository.existsById(employeeId)) {
            throw new UserNotFoundException("User not found with id: " + employeeId);
        }
        return travelRequestRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(travelRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    private TravelRequestEntity getTravelRequestById(Long id) {
        return travelRequestRepository.findById(id)
                .orElseThrow(() -> new TravelRequestNotFoundException("Travel request not found with id: " + id));
    }

    private String generateRequestNumber() {
        return "TR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

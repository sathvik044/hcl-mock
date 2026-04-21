package com.example.CorporateTravelManagementSystem.Service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.CorporateTravelManagementSystem.Exception.*;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDto;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestResponseDto;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.TravelStatus;
import com.example.CorporateTravelManagementSystem.mapper.TravelRequestMapper;
import com.example.CorporateTravelManagementSystem.Repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.Repository.UserRepository;
import com.example.CorporateTravelManagementSystem.Service.TravelRequestService;

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
        TravelRequestEntity travelRequest = findEntityById(id);

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
        TravelRequestEntity travelRequest = findEntityById(id);

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

    @Override
    public TravelRequestResponseDto approveByManager(Long id) {
        TravelRequestEntity travelRequest = findEntityById(id);
        if (travelRequest.getStatus() != TravelStatus.SUBMITTED) {
            throw new TravelRequestStateException("Only submitted requests can be approved by manager");
        }
        travelRequest.setStatus(TravelStatus.MANAGER_APPROVED);
        travelRequest.setUpdatedAt(LocalDateTime.now());
        return travelRequestMapper.toResponseDto(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestResponseDto rejectByManager(Long id) {
        TravelRequestEntity travelRequest = findEntityById(id);
        if (travelRequest.getStatus() != TravelStatus.SUBMITTED) {
            throw new TravelRequestStateException("Only submitted requests can be rejected by manager");
        }
        travelRequest.setStatus(TravelStatus.MANAGER_REJECTED);
        travelRequest.setUpdatedAt(LocalDateTime.now());
        return travelRequestMapper.toResponseDto(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestResponseDto approveByFinance(Long id) {
        TravelRequestEntity travelRequest = findEntityById(id);
        if (travelRequest.getStatus() != TravelStatus.MANAGER_APPROVED) {
            throw new TravelRequestStateException("Only manager approved requests can be approved by finance");
        }
        travelRequest.setStatus(TravelStatus.FINANCE_APPROVED);
        travelRequest.setUpdatedAt(LocalDateTime.now());
        return travelRequestMapper.toResponseDto(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestResponseDto rejectByFinance(Long id) {
        TravelRequestEntity travelRequest = findEntityById(id);
        if (travelRequest.getStatus() != TravelStatus.MANAGER_APPROVED) {
            throw new TravelRequestStateException("Only manager approved requests can be rejected by finance");
        }
        travelRequest.setStatus(TravelStatus.FINANCE_REJECTED);
        travelRequest.setUpdatedAt(LocalDateTime.now());
        return travelRequestMapper.toResponseDto(travelRequestRepository.save(travelRequest));
    }

    @Override
    public List<TravelRequestResponseDto> getBookedTravelRequests() {
        return travelRequestRepository.findByStatus(TravelStatus.BOOKED)
                .stream()
                .map(travelRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public TravelRequestResponseDto bookTravelRequest(Long id, com.example.CorporateTravelManagementSystem.dto.TravelBookingRequestDTO bookingRequest) {
        TravelRequestEntity travelRequest = findEntityById(id);
        if (travelRequest.getStatus() != TravelStatus.FINANCE_APPROVED) {
            throw new TravelRequestStateException("Only finance approved requests can be booked");
        }
        travelRequest.setStatus(TravelStatus.BOOKED);
        travelRequest.setUpdatedAt(LocalDateTime.now());
        return travelRequestMapper.toResponseDto(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestResponseDto getTravelRequestById(Long id) {
        return travelRequestMapper.toResponseDto(findEntityById(id));
    }

    private TravelRequestEntity findEntityById(Long id) {
        return travelRequestRepository.findById(id)
                .orElseThrow(() -> new TravelRequestNotFoundException("Travel request not found with id: " + id));
    }

    private String generateRequestNumber() {
        return "TR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

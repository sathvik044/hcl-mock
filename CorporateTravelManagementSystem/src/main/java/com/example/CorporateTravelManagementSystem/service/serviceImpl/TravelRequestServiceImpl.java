package com.example.CorporateTravelManagementSystem.Service.serviceImpl;

import com.example.CorporateTravelManagementSystem.dto.ApprovalActionRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelApproval;
import com.example.CorporateTravelManagementSystem.entity.TravelRequest;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.ApprovalStatus;
import com.example.CorporateTravelManagementSystem.enums.ApproverType;
import com.example.CorporateTravelManagementSystem.enums.TravelStatus;
import com.example.CorporateTravelManagementSystem.mapper.TravelRequestMapper;
import com.example.CorporateTravelManagementSystem.Repository.TravelApprovalRepository;
import com.example.CorporateTravelManagementSystem.Repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.Repository.UserRepository;
import com.example.CorporateTravelManagementSystem.Service.TravelRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelRequestServiceImpl implements TravelRequestService {

    private final TravelRequestRepository travelRequestRepository;
    private final TravelApprovalRepository travelApprovalRepository;
    private final UserRepository userRepository;
    private final TravelRequestMapper travelRequestMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TravelRequestDTO> getAllTravelRequests() {
        return travelRequestRepository.findAll().stream()
                .map(travelRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TravelRequestDTO> getTravelRequestById(Long id) {
        return travelRequestRepository.findById(id)
                .map(travelRequestMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelRequestDTO> getTravelRequestsByEmployee(Long employeeId) {
        User employee = findUser(employeeId, "User not found");
        return travelRequestRepository.findByEmployee(employee).stream()
                .map(travelRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelRequestDTO> getTravelRequestsByStatus(TravelStatus status) {
        return travelRequestRepository.findByStatus(status).stream()
                .map(travelRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TravelRequestDTO createTravelRequest(TravelRequestRequestDTO requestDTO) {
        validateTravelDates(requestDTO);
        User employee = findUser(requestDTO.getEmployeeId(), "User not found");
        TravelRequest travelRequest = travelRequestMapper.toEntity(requestDTO);
        travelRequest.setEmployee(employee);
        travelRequest.setStatus(TravelStatus.DRAFT);
        travelRequest.setSubmittedAt(null);
        TravelRequest savedRequest = travelRequestRepository.save(travelRequest);
        return travelRequestMapper.toDTO(savedRequest);
    }

    @Override
    public TravelRequestDTO updateTravelRequest(Long id, TravelRequestRequestDTO requestDTO) {
        validateTravelDates(requestDTO);
        TravelRequest existingRequest = findTravelRequest(id);
        User employee = findUser(requestDTO.getEmployeeId(), "User not found");

        TravelRequest updatedRequest = travelRequestMapper.toEntity(requestDTO);
        updatedRequest.setId(id);
        updatedRequest.setEmployee(employee);
        updatedRequest.setStatus(existingRequest.getStatus());
        updatedRequest.setSubmittedAt(existingRequest.getSubmittedAt());
        TravelRequest savedRequest = travelRequestRepository.save(updatedRequest);
        return travelRequestMapper.toDTO(savedRequest);
    }

    @Override
    public TravelRequestDTO submitTravelRequest(Long id) {
        TravelRequest travelRequest = findTravelRequest(id);
        ensureStatus(travelRequest, TravelStatus.DRAFT, "Only draft travel requests can be submitted");

        if (travelRequest.getEmployee().getManagerId() == null) {
            throw new IllegalStateException("Employee does not have a manager assigned");
        }

        travelRequest.setStatus(TravelStatus.SUBMITTED);
        travelRequest.setSubmittedAt(LocalDateTime.now());
        return travelRequestMapper.toDTO(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestDTO cancelTravelRequest(Long id) {
        TravelRequest travelRequest = findTravelRequest(id);

        if (travelRequest.getStatus() == TravelStatus.APPROVED
                || travelRequest.getStatus() == TravelStatus.REJECTED
                || travelRequest.getStatus() == TravelStatus.CANCELLED) {
            throw new IllegalStateException("Travel request cannot be cancelled in its current state");
        }

        travelRequest.setStatus(TravelStatus.CANCELLED);
        return travelRequestMapper.toDTO(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestDTO managerApproveTravelRequest(Long id, ApprovalActionRequestDTO requestDTO) {
        TravelRequest travelRequest = findTravelRequest(id);
        ensureStatus(travelRequest, TravelStatus.SUBMITTED, "Manager approval is only allowed for submitted requests");

        User approver = findUser(requestDTO.getApproverId(), "Approver not found");
        validateManagerApproval(travelRequest, approver);
        saveApprovalDecision(travelRequest, approver, ApproverType.MANAGER, ApprovalStatus.APPROVED, requestDTO.getRemarks());

        travelRequest.setStatus(TravelStatus.MANAGER_APPROVED);
        return travelRequestMapper.toDTO(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestDTO managerRejectTravelRequest(Long id, ApprovalActionRequestDTO requestDTO) {
        TravelRequest travelRequest = findTravelRequest(id);
        ensureStatus(travelRequest, TravelStatus.SUBMITTED, "Manager rejection is only allowed for submitted requests");

        User approver = findUser(requestDTO.getApproverId(), "Approver not found");
        validateManagerApproval(travelRequest, approver);
        saveApprovalDecision(travelRequest, approver, ApproverType.MANAGER, ApprovalStatus.REJECTED, requestDTO.getRemarks());

        travelRequest.setStatus(TravelStatus.REJECTED);
        return travelRequestMapper.toDTO(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestDTO financeApproveTravelRequest(Long id, ApprovalActionRequestDTO requestDTO) {
        TravelRequest travelRequest = findTravelRequest(id);
        ensureStatus(travelRequest, TravelStatus.MANAGER_APPROVED,
                "Finance approval is only allowed after manager approval");

        User approver = findUser(requestDTO.getApproverId(), "Approver not found");
        validateFinanceApprover(approver);
        saveApprovalDecision(travelRequest, approver, ApproverType.FINANCE, ApprovalStatus.APPROVED, requestDTO.getRemarks());

        travelRequest.setStatus(TravelStatus.APPROVED);
        return travelRequestMapper.toDTO(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestDTO financeRejectTravelRequest(Long id, ApprovalActionRequestDTO requestDTO) {
        TravelRequest travelRequest = findTravelRequest(id);
        ensureStatus(travelRequest, TravelStatus.MANAGER_APPROVED,
                "Finance rejection is only allowed after manager approval");

        User approver = findUser(requestDTO.getApproverId(), "Approver not found");
        validateFinanceApprover(approver);
        saveApprovalDecision(travelRequest, approver, ApproverType.FINANCE, ApprovalStatus.REJECTED, requestDTO.getRemarks());

        travelRequest.setStatus(TravelStatus.REJECTED);
        return travelRequestMapper.toDTO(travelRequestRepository.save(travelRequest));
    }

    @Override
    public TravelRequestDTO updateTravelRequestStatus(Long id, TravelStatus status) {
        if (status == TravelStatus.SUBMITTED) {
            return submitTravelRequest(id);
        }

        if (status == TravelStatus.CANCELLED) {
            return cancelTravelRequest(id);
        }

        if (status == TravelStatus.MANAGER_APPROVED || status == TravelStatus.APPROVED || status == TravelStatus.REJECTED) {
            throw new IllegalArgumentException("Use the approval endpoints to move the request to " + status);
        }

        TravelRequest travelRequest = findTravelRequest(id);
        travelRequest.setStatus(status);
        TravelRequest savedRequest = travelRequestRepository.save(travelRequest);
        return travelRequestMapper.toDTO(savedRequest);
    }

    @Override
    public void deleteTravelRequest(Long id) {
        travelRequestRepository.deleteById(id);
    }

    private TravelRequest findTravelRequest(Long id) {
        return travelRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Travel request not found"));
    }

    private User findUser(Long id, String message) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(message));
    }

    private void validateTravelDates(TravelRequestRequestDTO requestDTO) {
        if (requestDTO.getTravelStartDate() != null
                && requestDTO.getTravelEndDate() != null
                && requestDTO.getTravelEndDate().isBefore(requestDTO.getTravelStartDate())) {
            throw new IllegalArgumentException("Travel end date cannot be before the start date");
        }
    }

    private void ensureStatus(TravelRequest travelRequest, TravelStatus expectedStatus, String message) {
        if (travelRequest.getStatus() != expectedStatus) {
            throw new IllegalStateException(message);
        }
    }

    private void validateManagerApproval(TravelRequest travelRequest, User approver) {
        Long managerId = travelRequest.getEmployee().getManagerId();
        if (managerId == null || !managerId.equals(approver.getId())) {
            throw new IllegalArgumentException("Approver is not the employee's assigned manager");
        }

        if (!"MANAGER".equalsIgnoreCase(approver.getRole())) {
            throw new IllegalArgumentException("Approver must have the MANAGER role");
        }
    }

    private void validateFinanceApprover(User approver) {
        if (!"FINANCE".equalsIgnoreCase(approver.getRole())) {
            throw new IllegalArgumentException("Approver must have the FINANCE role");
        }
    }

    private void saveApprovalDecision(TravelRequest travelRequest, User approver, ApproverType approverType,
            ApprovalStatus status, String remarks) {
        TravelApproval approval = travelApprovalRepository.findByTravelRequestAndApproverType(travelRequest, approverType)
                .orElseGet(() -> TravelApproval.builder()
                        .travelRequest(travelRequest)
                        .approverType(approverType)
                        .build());

        approval.setApprover(approver);
        approval.setStatus(status);
        approval.setRemarks(remarks);
        approval.setApprovedAt(LocalDateTime.now());
        travelApprovalRepository.save(approval);
    }
}

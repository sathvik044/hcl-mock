package com.example.CorporateTravelManagementSystem.Service.serviceImpl;

import com.example.CorporateTravelManagementSystem.dto.TravelApprovalDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelApprovalRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelApproval;
import com.example.CorporateTravelManagementSystem.entity.TravelRequest;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.ApprovalStatus;
import com.example.CorporateTravelManagementSystem.enums.ApproverType;
import com.example.CorporateTravelManagementSystem.mapper.TravelApprovalMapper;
import com.example.CorporateTravelManagementSystem.mapper.TravelRequestMapper;
import com.example.CorporateTravelManagementSystem.mapper.UserMapper;
import com.example.CorporateTravelManagementSystem.Repository.TravelApprovalRepository;
import com.example.CorporateTravelManagementSystem.Repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.Repository.UserRepository;
import com.example.CorporateTravelManagementSystem.Service.TravelApprovalService;
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
public class TravelApprovalServiceImpl implements TravelApprovalService {

    private final TravelApprovalRepository travelApprovalRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final UserRepository userRepository;
    private final TravelApprovalMapper travelApprovalMapper;
    private final TravelRequestMapper travelRequestMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TravelApprovalDTO> getAllTravelApprovals() {
        return travelApprovalRepository.findAll().stream()
                .map(travelApprovalMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelApprovalDTO> getPendingManagerApprovals(Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new NoSuchElementException("Manager not found"));

        return travelRequestRepository.findByEmployee_ManagerIdAndStatus(managerId,
                        com.example.CorporateTravelManagementSystem.enums.TravelStatus.SUBMITTED)
                .stream()
                .map(travelRequest -> buildPendingApproval(travelRequest, ApproverType.MANAGER, manager))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelApprovalDTO> getPendingFinanceApprovals() {
        List<User> financeUsers = userRepository.findByRoleIgnoreCase("FINANCE");
        User financeApprover = financeUsers.size() == 1 ? financeUsers.get(0) : null;

        return travelRequestRepository.findByStatus(com.example.CorporateTravelManagementSystem.enums.TravelStatus.MANAGER_APPROVED)
                .stream()
                .map(travelRequest -> buildPendingApproval(travelRequest, ApproverType.FINANCE, financeApprover))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TravelApprovalDTO> getTravelApprovalById(Long id) {
        return travelApprovalRepository.findById(id)
                .map(travelApprovalMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelApprovalDTO> getApprovalsByTravelRequest(Long travelRequestId) {
        TravelRequest travelRequest = travelRequestRepository.findById(travelRequestId)
                .orElseThrow(() -> new NoSuchElementException("Travel request not found"));
        return travelApprovalRepository.findByTravelRequest(travelRequest).stream()
                .map(travelApprovalMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelApprovalDTO> getApprovalsByApprover(Long approverId) {
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return travelApprovalRepository.findByApprover(approver).stream()
                .map(travelApprovalMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TravelApprovalDTO createTravelApproval(TravelApprovalRequestDTO requestDTO) {
        TravelRequest travelRequest = travelRequestRepository.findById(requestDTO.getTravelRequestId())
                .orElseThrow(() -> new NoSuchElementException("Travel request not found"));
        User approver = userRepository.findById(requestDTO.getApproverId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        TravelApproval travelApproval = travelApprovalMapper.toEntity(requestDTO);
        travelApproval.setTravelRequest(travelRequest);
        travelApproval.setApprover(approver);
        if (travelApproval.getStatus() != null && travelApproval.getStatus() != ApprovalStatus.PENDING) {
            travelApproval.setApprovedAt(LocalDateTime.now());
        }
        TravelApproval savedApproval = travelApprovalRepository.save(travelApproval);
        return travelApprovalMapper.toDTO(savedApproval);
    }

    @Override
    public TravelApprovalDTO updateTravelApproval(Long id, TravelApprovalRequestDTO requestDTO) {
        TravelApproval existingApproval = travelApprovalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Travel approval not found"));
        TravelApproval updatedApproval = travelApprovalMapper.toEntity(requestDTO);
        updatedApproval.setId(id);
        updatedApproval.setTravelRequest(existingApproval.getTravelRequest());
        updatedApproval.setApprover(existingApproval.getApprover());
        if (updatedApproval.getStatus() != null && updatedApproval.getStatus() != ApprovalStatus.PENDING) {
            updatedApproval.setApprovedAt(LocalDateTime.now());
        }
        TravelApproval savedApproval = travelApprovalRepository.save(updatedApproval);
        return travelApprovalMapper.toDTO(savedApproval);
    }

    @Override
    public TravelApprovalDTO approveTravelRequest(Long travelRequestId, Long approverId, String remarks) {
        TravelRequest travelRequest = travelRequestRepository.findById(travelRequestId)
                .orElseThrow(() -> new NoSuchElementException("Travel request not found"));
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        TravelApproval approval = travelApprovalRepository
                .findByTravelRequestAndApproverType(travelRequest, ApproverType.MANAGER)
                .orElseGet(() -> TravelApproval.builder()
                        .travelRequest(travelRequest)
                        .approverType(ApproverType.MANAGER)
                        .build());
        approval.setApprover(approver);
        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setRemarks(remarks);
        approval.setApprovedAt(LocalDateTime.now());
        TravelApproval savedApproval = travelApprovalRepository.save(approval);
        return travelApprovalMapper.toDTO(savedApproval);
    }

    @Override
    public TravelApprovalDTO rejectTravelRequest(Long travelRequestId, Long approverId, String remarks) {
        TravelRequest travelRequest = travelRequestRepository.findById(travelRequestId)
                .orElseThrow(() -> new NoSuchElementException("Travel request not found"));
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        TravelApproval approval = travelApprovalRepository
                .findByTravelRequestAndApproverType(travelRequest, ApproverType.MANAGER)
                .orElseGet(() -> TravelApproval.builder()
                        .travelRequest(travelRequest)
                        .approverType(ApproverType.MANAGER)
                        .build());
        approval.setApprover(approver);
        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setRemarks(remarks);
        approval.setApprovedAt(LocalDateTime.now());
        TravelApproval savedApproval = travelApprovalRepository.save(approval);
        return travelApprovalMapper.toDTO(savedApproval);
    }

    @Override
    public void deleteTravelApproval(Long id) {
        travelApprovalRepository.deleteById(id);
    }

    private TravelApprovalDTO buildPendingApproval(TravelRequest travelRequest, ApproverType approverType, User approver) {
        return TravelApprovalDTO.builder()
                .travelRequest(travelRequestMapper.toDTO(travelRequest))
                .approverType(approverType)
                .approver(approver != null ? userMapper.toDTO(approver) : null)
                .status(ApprovalStatus.PENDING)
                .build();
    }
}

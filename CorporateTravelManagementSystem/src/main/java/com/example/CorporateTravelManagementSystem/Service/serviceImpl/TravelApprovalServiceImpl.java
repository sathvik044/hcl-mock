package com.example.CorporateTravelManagementSystem.Service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CorporateTravelManagementSystem.Exception.TravelApprovalNotFoundException;
import com.example.CorporateTravelManagementSystem.Repository.TravelApproovalRepository;
import com.example.CorporateTravelManagementSystem.Service.TravelApprovalService;
import com.example.CorporateTravelManagementSystem.entity.TravelApproval;
import com.example.CorporateTravelManagementSystem.enums.ApprovalStatus;

@Service
public class TravelApprovalServiceImpl implements TravelApprovalService {

    @Autowired
    private TravelApproovalRepository travelApprovalRepository;

    @Override
    public List<TravelApproval> getPendingApprovalsByManager(Long managerId) {
        return travelApprovalRepository.findByApprover_IdAndStatus(managerId, ApprovalStatus.PENDING);
    }

    @Override
    public List<TravelApproval> getPendingApprovalsByFinance() {
        return travelApprovalRepository.findByStatus(ApprovalStatus.PENDING);
    }

    @Override
    public TravelApproval approveByManager(Long id) {
        TravelApproval travelApproval = getTravelApproval(id);
        travelApproval.setStatus(ApprovalStatus.APPROVED);
        return travelApprovalRepository.save(travelApproval);
    }

    @Override
    public TravelApproval rejectByManager(Long id) {
        TravelApproval travelApproval = getTravelApproval(id);
        travelApproval.setStatus(ApprovalStatus.REJECTED);
        return travelApprovalRepository.save(travelApproval);
    }

    @Override
    public TravelApproval approveByFinance(Long id) {
        TravelApproval travelApproval = getTravelApproval(id);
        travelApproval.setStatus(ApprovalStatus.APPROVED);
        return travelApprovalRepository.save(travelApproval);
    }

    @Override
    public TravelApproval rejectByFinance(Long id) {
        TravelApproval travelApproval = getTravelApproval(id);
        travelApproval.setStatus(ApprovalStatus.REJECTED);
        return travelApprovalRepository.save(travelApproval);
    }

    private TravelApproval getTravelApproval(Long id) {
        return travelApprovalRepository.findById(id)
                .orElseThrow(() -> new TravelApprovalNotFoundException("Travel approval not found with id: " + id));
    }
}

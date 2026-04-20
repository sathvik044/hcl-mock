package com.example.CorporateTravelManagementSystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CorporateTravelManagementSystem.entity.TravelApproval;
import com.example.CorporateTravelManagementSystem.enums.ApprovalStatus;

public interface TravelApproovalRepository extends JpaRepository<TravelApproval, Long>    {
    List<TravelApproval> findByApprover_IdAndStatus(Long approverId, ApprovalStatus status);
    List<TravelApproval> findByStatus(ApprovalStatus status);
}

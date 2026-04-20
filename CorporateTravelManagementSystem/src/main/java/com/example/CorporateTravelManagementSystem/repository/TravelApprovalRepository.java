package com.example.CorporateTravelManagementSystem.Repository;

import com.example.CorporateTravelManagementSystem.entity.TravelApproval;
import com.example.CorporateTravelManagementSystem.entity.TravelRequest;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.ApprovalStatus;
import com.example.CorporateTravelManagementSystem.enums.ApproverType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelApprovalRepository extends JpaRepository<TravelApproval, Long> {
    List<TravelApproval> findByTravelRequest(TravelRequest travelRequest);

    List<TravelApproval> findByApprover(User approver);

    List<TravelApproval> findByStatus(ApprovalStatus status);

    List<TravelApproval> findByTravelRequestAndStatus(TravelRequest travelRequest, ApprovalStatus status);

    Optional<TravelApproval> findByTravelRequestAndApproverType(TravelRequest travelRequest, ApproverType approverType);
}

package com.example.CorporateTravelManagementSystem.Repository;

import com.example.CorporateTravelManagementSystem.entity.TravelRequest;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.TravelStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {
    List<TravelRequest> findByEmployee(User employee);

    List<TravelRequest> findByStatus(TravelStatus status);

    List<TravelRequest> findByEmployeeAndStatus(User employee, TravelStatus status);

    List<TravelRequest> findByEmployee_ManagerIdAndStatus(Long managerId, TravelStatus status);
}

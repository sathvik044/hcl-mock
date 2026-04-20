package com.example.CorporateTravelManagementSystem.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.CorporateTravelManagementSystem.Service.TravelApprovalService;
import com.example.CorporateTravelManagementSystem.entity.TravelApproval;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travel-approvals")
public class TravelApprovalController {
    public TravelApprovalService travelApprovalService;

   @GetMapping("/manager/{managerId}/pending")
    public List<TravelApproval> getPendingApprovalsByManager(Long managerId) {
        return travelApprovalService.getPendingApprovalsByManager(managerId);
    }
    @GetMapping("/finance/pending")
    public List<TravelApproval> getPendingApprovalsByFinance() {
        return travelApprovalService.getPendingApprovalsByFinance();
    }   
    @PutMapping("/{id}/approve/manager")
    public TravelApproval approveByManager(@PathVariable Long id) {
        return travelApprovalService.approveByManager(id);
    }
    @PutMapping("/{id}/reject/manager")
    public TravelApproval rejectByManager(@PathVariable Long id) {
        return travelApprovalService.rejectByManager(id);
    }
    @PutMapping("/{id}/approve/finance")
    public TravelApproval approveByFinance(@PathVariable Long id) {
        return travelApprovalService.approveByFinance(id);
    }   
}

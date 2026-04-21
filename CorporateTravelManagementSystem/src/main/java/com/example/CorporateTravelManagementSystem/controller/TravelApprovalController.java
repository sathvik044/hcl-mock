package com.example.CorporateTravelManagementSystem.Controller;

import com.example.CorporateTravelManagementSystem.dto.TravelApprovalDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelApprovalRequestDTO;
import com.example.CorporateTravelManagementSystem.Service.TravelApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({ "/api/travel-approvals", "/api/approvals" })
@RequiredArgsConstructor
public class TravelApprovalController {

    private final TravelApprovalService travelApprovalService;

    @GetMapping
    public ResponseEntity<List<TravelApprovalDTO>> getAllTravelApprovals() {
        return ResponseEntity.ok(travelApprovalService.getAllTravelApprovals());
    }

    @GetMapping("/pending/manager/{managerId}")
    public ResponseEntity<List<TravelApprovalDTO>> getPendingManagerApprovals(@PathVariable Long managerId) {
        return ResponseEntity.ok(travelApprovalService.getPendingManagerApprovals(managerId));
    }

    @GetMapping("/pending/finance")
    public ResponseEntity<List<TravelApprovalDTO>> getPendingFinanceApprovals() {
        return ResponseEntity.ok(travelApprovalService.getPendingFinanceApprovals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelApprovalDTO> getTravelApprovalById(@PathVariable Long id) {
        return travelApprovalService.getTravelApprovalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/travel-request/{travelRequestId}")
    public ResponseEntity<List<TravelApprovalDTO>> getApprovalsByTravelRequest(@PathVariable Long travelRequestId) {
        return ResponseEntity.ok(travelApprovalService.getApprovalsByTravelRequest(travelRequestId));
    }

    @GetMapping("/approver/{approverId}")
    public ResponseEntity<List<TravelApprovalDTO>> getApprovalsByApprover(@PathVariable Long approverId) {
        return ResponseEntity.ok(travelApprovalService.getApprovalsByApprover(approverId));
    }

    @PostMapping
    public ResponseEntity<TravelApprovalDTO> createTravelApproval(
            @Valid @RequestBody TravelApprovalRequestDTO requestDTO) {
        TravelApprovalDTO travelApprovalDTO = travelApprovalService.createTravelApproval(requestDTO);
        return ResponseEntity.ok(travelApprovalDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelApprovalDTO> updateTravelApproval(@PathVariable Long id,
            @Valid @RequestBody TravelApprovalRequestDTO requestDTO) {
        TravelApprovalDTO travelApprovalDTO = travelApprovalService.updateTravelApproval(id, requestDTO);
        return ResponseEntity.ok(travelApprovalDTO);
    }

    @PostMapping("/approve/{travelRequestId}")
    public ResponseEntity<TravelApprovalDTO> approveTravelRequest(@PathVariable Long travelRequestId,
            @RequestParam Long approverId,
            @RequestParam(required = false) String remarks) {
        TravelApprovalDTO travelApprovalDTO = travelApprovalService.approveTravelRequest(travelRequestId, approverId,
                remarks);
        return ResponseEntity.ok(travelApprovalDTO);
    }

    @PostMapping("/reject/{travelRequestId}")
    public ResponseEntity<TravelApprovalDTO> rejectTravelRequest(@PathVariable Long travelRequestId,
            @RequestParam Long approverId,
            @RequestParam(required = false) String remarks) {
        TravelApprovalDTO travelApprovalDTO = travelApprovalService.rejectTravelRequest(travelRequestId, approverId,
                remarks);
        return ResponseEntity.ok(travelApprovalDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelApproval(@PathVariable Long id) {
        travelApprovalService.deleteTravelApproval(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.CorporateTravelManagementSystem.Controller;

import com.example.CorporateTravelManagementSystem.dto.ApprovalActionRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDTO;
import com.example.CorporateTravelManagementSystem.enums.TravelStatus;
import com.example.CorporateTravelManagementSystem.Service.TravelRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel-requests")
@RequiredArgsConstructor
public class TravelRequestController {

    private final TravelRequestService travelRequestService;

    @GetMapping
    public ResponseEntity<List<TravelRequestDTO>> getAllTravelRequests() {
        return ResponseEntity.ok(travelRequestService.getAllTravelRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelRequestDTO> getTravelRequestById(@PathVariable Long id) {
        return travelRequestService.getTravelRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<TravelRequestDTO>> getTravelRequestsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(travelRequestService.getTravelRequestsByEmployee(employeeId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TravelRequestDTO>> getTravelRequestsByStatus(@PathVariable TravelStatus status) {
        return ResponseEntity.ok(travelRequestService.getTravelRequestsByStatus(status));
    }

    @PostMapping
    public ResponseEntity<TravelRequestDTO> createTravelRequest(@Valid @RequestBody TravelRequestRequestDTO requestDTO) {
        TravelRequestDTO travelRequestDTO = travelRequestService.createTravelRequest(requestDTO);
        return ResponseEntity.ok(travelRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelRequestDTO> updateTravelRequest(@PathVariable Long id,
            @Valid @RequestBody TravelRequestRequestDTO requestDTO) {
        TravelRequestDTO travelRequestDTO = travelRequestService.updateTravelRequest(id, requestDTO);
        return ResponseEntity.ok(travelRequestDTO);
    }

    @PutMapping("/{id}/submit")
    public ResponseEntity<TravelRequestDTO> submitTravelRequest(@PathVariable Long id) {
        return ResponseEntity.ok(travelRequestService.submitTravelRequest(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<TravelRequestDTO> cancelTravelRequest(@PathVariable Long id) {
        return ResponseEntity.ok(travelRequestService.cancelTravelRequest(id));
    }

    @PutMapping("/{id}/manager-approve")
    public ResponseEntity<TravelRequestDTO> managerApproveTravelRequest(@PathVariable Long id,
            @Valid @RequestBody ApprovalActionRequestDTO requestDTO) {
        return ResponseEntity.ok(travelRequestService.managerApproveTravelRequest(id, requestDTO));
    }

    @PutMapping("/{id}/manager-reject")
    public ResponseEntity<TravelRequestDTO> managerRejectTravelRequest(@PathVariable Long id,
            @Valid @RequestBody ApprovalActionRequestDTO requestDTO) {
        return ResponseEntity.ok(travelRequestService.managerRejectTravelRequest(id, requestDTO));
    }

    @PutMapping("/{id}/finance-approve")
    public ResponseEntity<TravelRequestDTO> financeApproveTravelRequest(@PathVariable Long id,
            @Valid @RequestBody ApprovalActionRequestDTO requestDTO) {
        return ResponseEntity.ok(travelRequestService.financeApproveTravelRequest(id, requestDTO));
    }

    @PutMapping("/{id}/finance-reject")
    public ResponseEntity<TravelRequestDTO> financeRejectTravelRequest(@PathVariable Long id,
            @Valid @RequestBody ApprovalActionRequestDTO requestDTO) {
        return ResponseEntity.ok(travelRequestService.financeRejectTravelRequest(id, requestDTO));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TravelRequestDTO> updateTravelRequestStatus(@PathVariable Long id,
            @RequestBody TravelStatus status) {
        TravelRequestDTO travelRequestDTO = travelRequestService.updateTravelRequestStatus(id, status);
        return ResponseEntity.ok(travelRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelRequest(@PathVariable Long id) {
        travelRequestService.deleteTravelRequest(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.CorporateTravelManagementSystem.Controller;

import com.example.CorporateTravelManagementSystem.dto.BudgetUtilizationDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetRequestDTO;
import com.example.CorporateTravelManagementSystem.Service.TravelBudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping({"/api/travel-budgets", "/api/budgets"})
@RequiredArgsConstructor
public class TravelBudgetController {

    private final TravelBudgetService travelBudgetService;

    @GetMapping
    public ResponseEntity<List<TravelBudgetDTO>> getAllTravelBudgets() {
        return ResponseEntity.ok(travelBudgetService.getAllTravelBudgets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelBudgetDTO> getTravelBudgetById(@PathVariable Long id) {
        return travelBudgetService.getTravelBudgetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<TravelBudgetDTO>> getBudgetsByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(travelBudgetService.getBudgetsByDepartment(department));
    }

    @GetMapping("/cost-center/{costCenter}")
    public ResponseEntity<List<TravelBudgetDTO>> getBudgetsByCostCenter(@PathVariable String costCenter) {
        return ResponseEntity.ok(travelBudgetService.getBudgetsByCostCenter(costCenter));
    }

    @GetMapping("/utilization")
    public ResponseEntity<BudgetUtilizationDTO> getBudgetUtilization() {
        return ResponseEntity.ok(travelBudgetService.getBudgetUtilization());
    }

    @GetMapping("/year/{financialYear}")
    public ResponseEntity<List<TravelBudgetDTO>> getBudgetsByFinancialYear(@PathVariable String financialYear) {
        return ResponseEntity.ok(travelBudgetService.getBudgetsByFinancialYear(financialYear));
    }

    @GetMapping("/department/{department}/year/{financialYear}")
    public ResponseEntity<TravelBudgetDTO> getBudgetByDepartmentAndYear(@PathVariable String department,
            @PathVariable String financialYear) {
        return travelBudgetService.getBudgetByDepartmentAndYear(department, financialYear)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TravelBudgetDTO> createTravelBudget(@Valid @RequestBody TravelBudgetRequestDTO requestDTO) {
        TravelBudgetDTO travelBudgetDTO = travelBudgetService.createTravelBudget(requestDTO);
        return ResponseEntity.ok(travelBudgetDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelBudgetDTO> updateTravelBudget(@PathVariable Long id,
            @Valid @RequestBody TravelBudgetRequestDTO requestDTO) {
        TravelBudgetDTO travelBudgetDTO = travelBudgetService.updateTravelBudget(id, requestDTO);
        return ResponseEntity.ok(travelBudgetDTO);
    }

    @PutMapping("/{id}/utilize")
    public ResponseEntity<TravelBudgetDTO> updateUtilizedAmount(@PathVariable Long id,
            @RequestParam BigDecimal amount) {
        TravelBudgetDTO travelBudgetDTO = travelBudgetService.updateUtilizedAmount(id, amount);
        return ResponseEntity.ok(travelBudgetDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravelBudget(@PathVariable Long id) {
        travelBudgetService.deleteTravelBudget(id);
        return ResponseEntity.noContent().build();
    }
}

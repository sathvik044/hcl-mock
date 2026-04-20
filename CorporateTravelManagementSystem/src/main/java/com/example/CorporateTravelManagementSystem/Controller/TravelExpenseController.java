package com.example.CorporateTravelManagementSystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.CorporateTravelManagementSystem.Service.TravelExpenseService;
import com.example.CorporateTravelManagementSystem.dto.TravelExpenseRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelExpenseResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class TravelExpenseController {

    private final TravelExpenseService expenseService;

    @PostMapping
    public TravelExpenseResponseDTO create(@RequestBody TravelExpenseRequestDTO dto) {
        return expenseService.create(dto);
    }

    @GetMapping("/travel-request/{id}")
    public List<TravelExpenseResponseDTO> getByRequest(@PathVariable Long id) {
        return expenseService.getByRequest(id);
    }

    @PutMapping("/{id}/verify")
    public TravelExpenseResponseDTO verify(@PathVariable Long id,
                                           @RequestParam Long financeUserId) {
        return expenseService.verify(id, financeUserId);
    }

    @PutMapping("/{id}/reimburse")
    public TravelExpenseResponseDTO reimburse(@PathVariable Long id) {
        return expenseService.reimburse(id);
    }
}

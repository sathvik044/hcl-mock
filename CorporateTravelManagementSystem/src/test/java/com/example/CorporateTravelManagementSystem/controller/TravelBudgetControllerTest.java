package com.example.CorporateTravelManagementSystem.controller;

import com.example.CorporateTravelManagementSystem.dto.TravelBudgetDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelBudgetRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelBudget;
import com.example.CorporateTravelManagementSystem.repository.TravelBudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TravelBudgetControllerTest {

    @Autowired
    private TravelBudgetRepository travelBudgetRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        travelBudgetRepository.deleteAll();
    }

    @Test
    public void testCreateTravelBudget() throws Exception {
        mockMvc.perform(post("/api/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "department": "IT",
                          "costCenter": "CC001",
                          "financialYear": "2024-2025",
                          "totalAllocated": 100000
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(jsonPath("$.totalAllocated").value(100000));
    }

    @Test
    public void testGetAllTravelBudgets() throws Exception {
        TravelBudget budget = TravelBudget.builder()
                .department("HR")
                .costCenter("CC002")
                .financialYear("2024-2025")
                .totalAllocated(BigDecimal.valueOf(50000))
                .totalUtilized(BigDecimal.ZERO)
                .remainingBalance(BigDecimal.valueOf(50000))
                .build();
        travelBudgetRepository.save(budget);

        mockMvc.perform(get("/api/budgets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").value("HR"));
    }

    @Test
    public void testGetTravelBudgetById() throws Exception {
        TravelBudget budget = TravelBudget.builder()
                .department("Finance")
                .costCenter("CC003")
                .financialYear("2024-2025")
                .totalAllocated(BigDecimal.valueOf(75000))
                .totalUtilized(BigDecimal.ZERO)
                .remainingBalance(BigDecimal.valueOf(75000))
                .build();
        TravelBudget savedBudget = travelBudgetRepository.save(budget);

        mockMvc.perform(get("/api/budgets/" + savedBudget.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("Finance"));
    }

    @Test
    public void testUpdateUtilizedAmount() throws Exception {
        TravelBudget budget = TravelBudget.builder()
                .department("Marketing")
                .costCenter("CC004")
                .financialYear("2024-2025")
                .totalAllocated(BigDecimal.valueOf(60000))
                .totalUtilized(BigDecimal.ZERO)
                .remainingBalance(BigDecimal.valueOf(60000))
                .build();
        TravelBudget savedBudget = travelBudgetRepository.save(budget);

        mockMvc.perform(put("/api/budgets/" + savedBudget.getId() + "/utilize")
                .param("amount", "15000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUtilized").value(15000))
                .andExpect(jsonPath("$.remainingBalance").value(45000));
    }

    @Test
    public void testGetBudgetsByCostCenter() throws Exception {
        TravelBudget budget = TravelBudget.builder()
                .department("Operations")
                .costCenter("OPS-01")
                .financialYear("2025-2026")
                .totalAllocated(BigDecimal.valueOf(90000))
                .totalUtilized(BigDecimal.valueOf(10000))
                .remainingBalance(BigDecimal.valueOf(80000))
                .build();
        travelBudgetRepository.save(budget);

        mockMvc.perform(get("/api/budgets/cost-center/OPS-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").value("Operations"))
                .andExpect(jsonPath("$[0].costCenter").value("OPS-01"));
    }

    @Test
    public void testGetBudgetUtilization() throws Exception {
        travelBudgetRepository.save(TravelBudget.builder()
                .department("IT")
                .costCenter("IT-01")
                .financialYear("2025-2026")
                .totalAllocated(BigDecimal.valueOf(100000))
                .totalUtilized(BigDecimal.valueOf(25000))
                .remainingBalance(BigDecimal.valueOf(75000))
                .build());
        travelBudgetRepository.save(TravelBudget.builder()
                .department("Finance")
                .costCenter("FIN-01")
                .financialYear("2025-2026")
                .totalAllocated(BigDecimal.valueOf(50000))
                .totalUtilized(BigDecimal.valueOf(10000))
                .remainingBalance(BigDecimal.valueOf(40000))
                .build());

        mockMvc.perform(get("/api/budgets/utilization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgetCount").value(2))
                .andExpect(jsonPath("$.totalAllocated").value(150000))
                .andExpect(jsonPath("$.totalUtilized").value(35000))
                .andExpect(jsonPath("$.totalRemaining").value(115000))
                .andExpect(jsonPath("$.utilizationPercentage").value(23.33));
    }
}

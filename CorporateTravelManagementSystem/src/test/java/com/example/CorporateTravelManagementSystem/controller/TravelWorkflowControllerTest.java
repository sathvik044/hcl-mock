package com.example.CorporateTravelManagementSystem.controller;

import com.example.CorporateTravelManagementSystem.dto.ApprovalActionRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelRequestRequestDTO;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.repository.TravelApprovalRepository;
import com.example.CorporateTravelManagementSystem.repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TravelWorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TravelRequestRepository travelRequestRepository;

    @Autowired
    private TravelApprovalRepository travelApprovalRepository;

    private User manager;
    private User finance;
    private User employee;

    @BeforeEach
    public void setup() {
        travelApprovalRepository.deleteAll();
        travelRequestRepository.deleteAll();
        userRepository.deleteAll();

        manager = userRepository.save(User.builder()
                .firstName("Riya")
                .lastName("Manager")
                .email("riya.manager@example.com")
                .department("Engineering")
                .role("MANAGER")
                .employeeId("MGR-100")
                .build());
        finance = userRepository.save(User.builder()
                .firstName("Farah")
                .lastName("Finance")
                .email("farah.finance@example.com")
                .department("Finance")
                .role("FINANCE")
                .employeeId("FIN-100")
                .build());
        employee = userRepository.save(User.builder()
                .firstName("Arun")
                .lastName("Employee")
                .email("arun.employee@example.com")
                .department("Engineering")
                .role("EMPLOYEE")
                .employeeId("EMP-100")
                .managerId(manager.getId())
                .build());
    }

    @Test
    public void testTravelRequestApprovalWorkflow() throws Exception {
        Long requestId = createTravelRequest();

        mockMvc.perform(get("/api/travel-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("DRAFT"));

        mockMvc.perform(get("/api/travel-requests/employee/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee.id").value(employee.getId()));

        mockMvc.perform(put("/api/travel-requests/" + requestId + "/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUBMITTED"));

        mockMvc.perform(get("/api/approvals/pending/manager/" + manager.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].travelRequest.id").value(requestId))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].approver.id").value(manager.getId()));

        mockMvc.perform(put("/api/travel-requests/" + requestId + "/manager-approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(approvalActionJson(manager.getId(), "Manager approved")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("MANAGER_APPROVED"));

        mockMvc.perform(get("/api/approvals/pending/finance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].travelRequest.id").value(requestId))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        mockMvc.perform(put("/api/travel-requests/" + requestId + "/finance-approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(approvalActionJson(finance.getId(), "Finance approved")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    public void testManagerRejectFinanceRejectAndCancelEndpoints() throws Exception {
        Long managerRejectRequestId = createTravelRequest();
        mockMvc.perform(put("/api/travel-requests/" + managerRejectRequestId + "/submit"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/travel-requests/" + managerRejectRequestId + "/manager-reject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(approvalActionJson(manager.getId(), "Policy mismatch")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));

        Long financeRejectRequestId = createTravelRequest();
        mockMvc.perform(put("/api/travel-requests/" + financeRejectRequestId + "/submit"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/travel-requests/" + financeRejectRequestId + "/manager-approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(approvalActionJson(manager.getId(), "Approved for finance review")))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/travel-requests/" + financeRejectRequestId + "/finance-reject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(approvalActionJson(finance.getId(), "Budget exhausted")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));

        Long cancelRequestId = createTravelRequest();
        mockMvc.perform(put("/api/travel-requests/" + cancelRequestId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    private Long createTravelRequest() throws Exception {
        mockMvc.perform(post("/api/travel-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "employeeId": %d,
                          "destination": "Bangalore",
                          "travelStartDate": "2026-05-10",
                          "travelEndDate": "2026-05-14",
                          "purpose": "Client meeting",
                          "estimatedCost": 25000,
                          "remarks": "Need travel approval"
                        }
                        """.formatted(employee.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DRAFT"));

        return travelRequestRepository.findAll().stream()
                .map(travelRequest -> travelRequest.getId())
                .max(Long::compareTo)
                .orElseThrow();
    }

    private String approvalActionJson(Long approverId, String remarks) {
        return """
                {
                  "approverId": %d,
                  "remarks": "%s"
                }
                """.formatted(approverId, remarks);
    }
}

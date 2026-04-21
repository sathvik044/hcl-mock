package com.example.CorporateTravelManagementSystem.controller;

import com.example.CorporateTravelManagementSystem.dto.UserRequestDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TravelRequestRepository travelRequestRepository;

    @Autowired
    private TravelApprovalRepository travelApprovalRepository;

    @BeforeEach
    public void setup() {
        travelApprovalRepository.deleteAll();
        travelRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUserAndFilterByRole() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Maya",
                          "lastName": "Manager",
                          "email": "maya.manager@example.com",
                          "department": "IT",
                          "role": "MANAGER",
                          "employeeId": "EMP-MGR-001"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("MANAGER"));

        mockMvc.perform(get("/api/users").param("role", "MANAGER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("maya.manager@example.com"));
    }

    @Test
    public void testGetTeamMembers() throws Exception {
        User manager = userRepository.save(User.builder()
                .firstName("Nina")
                .lastName("Lead")
                .email("nina.lead@example.com")
                .department("Engineering")
                .role("MANAGER")
                .employeeId("EMP-MGR-002")
                .build());
        userRepository.save(User.builder()
                .firstName("Ethan")
                .lastName("Dev")
                .email("ethan.dev@example.com")
                .department("Engineering")
                .role("EMPLOYEE")
                .employeeId("EMP-001")
                .managerId(manager.getId())
                .build());

        mockMvc.perform(get("/api/users/" + manager.getId() + "/team"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].managerId").value(manager.getId()))
                .andExpect(jsonPath("$[0].email").value("ethan.dev@example.com"));
    }
}

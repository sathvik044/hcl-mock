package com.example.CorporateTravelManagementSystem.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.example.CorporateTravelManagementSystem.enums.TravelType;
import com.example.CorporateTravelManagementSystem.enums.TravelPurpose;
import com.example.CorporateTravelManagementSystem.enums.TravelStatus;
@Entity
@Table(name = "travel_requests")
@Data
public class TravelRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String requestNumber;

    
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Enumerated(EnumType.STRING)
    private TravelType travelType;

    @Enumerated(EnumType.STRING)
    private TravelPurpose purpose;

    private LocalDate fromDate;
    private LocalDate toDate;

    private String fromCity;
    private String toCity;

    private BigDecimal estimatedCost;
    private BigDecimal actualCost;

    @Enumerated(EnumType.STRING)
    private TravelStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

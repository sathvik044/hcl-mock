package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelActivityLogResponseDTO {

    private Long id;
    private Long travelRequestId;

    private String action;
    private Long performedBy;

    private String oldStatus;
    private String newStatus;

    private LocalDateTime timestamp;
}
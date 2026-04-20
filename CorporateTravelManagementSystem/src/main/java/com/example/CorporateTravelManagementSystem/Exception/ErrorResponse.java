package com.example.CorporateTravelManagementSystem.Exception;

import java.time.LocalDateTime;
import lombok.*;
@Data
@AllArgsConstructor

public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

   
}

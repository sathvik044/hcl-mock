package com.example.CorporateTravelManagementSystem.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.CONFLICT, request.getRequestURI());
    }

    @ExceptionHandler(TravelRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTravelRequestNotFoundException(
            TravelRequestNotFoundException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    @ExceptionHandler(TravelRequestStateException.class)
    public ResponseEntity<ErrorResponse> handleTravelRequestStateException(
            TravelRequestStateException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(TravelExpenseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTravelExpenseNotFoundException(
            TravelExpenseNotFoundException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    @ExceptionHandler(TravelApprovalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTravelApprovalNotFoundException(
            TravelApprovalNotFoundException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    @ExceptionHandler({TravelExpenseStateException.class, TravelBookingStateException.class, InvalidUserRoleException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(
            RuntimeException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            HttpServletRequest request) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, String path) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path);

        return new ResponseEntity<>(errorResponse, status);
    }
}

package com.fawry.bank_api.exception;

import static org.springframework.http.HttpStatus.*;

import com.fawry.bank_api.dto.error.ErrorResponseDTO;
import com.fawry.bank_api.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles EntityNotFoundException and returns a custom error response.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        return buildErrorResponse(ErrorCode.ENTITY_NOT_FOUND, e.getMessage(), NOT_FOUND);
    }

    /**
     * Handles DuplicateResourceException and returns a custom error response.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateResourceException(DuplicateResourceException e) {
        Map<String, String> details = new LinkedHashMap<>();

        details.put("resource", e.getResource().name());

        if(e.getResource().name().compareToIgnoreCase("email") == 0)
            details.put("email", e.getMessage());

        return buildErrorResponse(ErrorCode.DUPLICATE_RESOURCE, e.getMessage(), CONFLICT, details);
    }

    /**
     * Handles IllegalActionException and returns a custom error response.
     */
    @ExceptionHandler(IllegalActionException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalActionException(IllegalActionException e) {
        return buildErrorResponse(ErrorCode.ILLEGAL_ACTION, e.getMessage(), BAD_REQUEST);
    }

    /**
     * Handles MissingServletRequestParameterException and returns a custom error response.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingRequestParameterException(MissingServletRequestParameterException e) {
        Map<String, String> details = new LinkedHashMap<>();
        details.put("name", e.getParameterName());
        details.put("type", e.getParameterType());
        return buildErrorResponse(ErrorCode.MISSING_PARAMETER, "Missing request parameter", BAD_REQUEST, details);
    }

    /**
     * Handles MethodArgumentTypeMismatchException and returns a custom error response.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, String> details = new LinkedHashMap<>();
        details.put("requiredType", e.getRequiredType().getSimpleName());
        details.put("passedValue", e.getValue().toString());
        return buildErrorResponse(ErrorCode.TYPE_MISMATCH, "Method argument type mismatch", BAD_REQUEST, details);
    }

    /**
     * Handles MethodArgumentNotValidException (validation errors) and returns a custom error response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return buildErrorResponse(ErrorCode.VALIDATION_ERROR, "Validation error", BAD_REQUEST, errors);
    }

    /**
     * Handles AccessDeniedException and returns a custom error response.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException e) {
        return buildErrorResponse(ErrorCode.ACCESS_DENIED, e.getMessage(), FORBIDDEN);
    }

    /**
     * Handles AuthenticationException and returns a custom error response.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException e) {
        return buildErrorResponse(ErrorCode.UNAUTHORIZED, e.getMessage(), UNAUTHORIZED);
    }


    /**
     * Builds the common error response structure for all exceptions.
     */
    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(ErrorCode errorCode, String message, HttpStatus status) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ErrorResponseDTO errorResponse = ErrorResponseDTO.createErrorResponse(
                status,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Builds the error response structure for all exceptions with additional details (e.g., validation errors).
     */
    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(ErrorCode errorCode, String message, HttpStatus status, Map<String, String> details) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .fieldErrors(details)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}

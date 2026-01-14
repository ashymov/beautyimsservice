package com.sashymov.beautyimsservice.api;

import com.sashymov.beautyimsservice.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(
            BusinessException ex,
            HttpServletRequest req
    ) {
        HttpStatus status = mapStatus(ex);

        return ResponseEntity.status(status).body(
                new ApiError(
                        ex.getCode(),
                        ex.getMessage(),
                        req.getRequestURI(),
                        OffsetDateTime.now()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception ex,
            HttpServletRequest req
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiError(
                        "INTERNAL_ERROR",
                        "Unexpected error",
                        req.getRequestURI(),
                        OffsetDateTime.now()
                )
        );
    }

    private HttpStatus mapStatus(BusinessException ex) {
        return switch (ex.getCode()) {
            case "TIME_SLOT_BUSY" -> HttpStatus.CONFLICT;
            case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "INVALID_TIME_RANGE", "OUTSIDE_WORKING_HOURS" -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.BAD_REQUEST;
        };
    }
}

package com.sashymov.beautyimsservice.api;

import java.time.OffsetDateTime;

public class ApiError {
    private String code;
    private String message;
    private String path;
    private OffsetDateTime timestamp;

    public ApiError(String code, String message, String path, OffsetDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}

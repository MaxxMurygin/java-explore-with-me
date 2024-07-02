package ru.practicum.ewm.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiError {
    private String errors;
    private String message;
    private String reason;
    private HttpStatus status;
    private String timestamp;
}

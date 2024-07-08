package ru.practicum.ewm.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String reason;
    private String message;
    private String timestamp;
    private String errors;
}

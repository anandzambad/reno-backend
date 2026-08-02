package com.reno.common.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorBody> validation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField()+": "+e.getDefaultMessage()).toList();
        return new ApiResponse<>(false, new ErrorBody("VALIDATION_ERROR", "Request validation failed", details), "Validation failed");
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<ErrorBody> notFound(ResourceNotFoundException ex) {
        return new ApiResponse<>(false, new ErrorBody("NOT_FOUND", ex.getMessage(), List.of()), ex.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<ErrorBody> badRequest(IllegalArgumentException ex) {
        return new ApiResponse<>(false, new ErrorBody("BAD_REQUEST", ex.getMessage(), List.of()), ex.getMessage());
    }
    public record ErrorBody(String code, String message, List<String> details) {}
}

package org.example.soundwave.config;

import lombok.extern.slf4j.Slf4j;
import org.example.soundwave.exceptions.AlreadyExistsException;
import org.example.soundwave.exceptions.DoesNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", message);
        return errors;
    }

    private Map<String, String> createValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        return createValidationErrors(ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleGeneralException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return createErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExistsException.class)
    public Map<String, String> handleAlreadyExistsException(AlreadyExistsException ex) {
        log.error("id already exists: {}", ex.getMessage());
        return createErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DoesNotExistsException.class)
    public Map<String, String> handleDoesNotExistsException(DoesNotExistsException ex) {
        log.error("Id does not exist: {}", ex.getMessage());
        return createErrorResponse(ex.getMessage());
    }
}
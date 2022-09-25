package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(final FieldValidationException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse("Request processing error!", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongStatusError(IllegalArgumentException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongStatusError(ConstraintViolationException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse("Parameter validation error!", exception.getMessage());
    }
}

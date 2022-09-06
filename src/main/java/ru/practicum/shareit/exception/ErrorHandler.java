package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

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
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleFieldError(final FieldUniqueException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse("Field unique error!", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserValidationError(final UserValidationException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse("User validation error!", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserDoesntExistError(final UserDoesntExistException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse("User validation error!", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse("Entity not found!", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongStatusError(IllegalArgumentException exception) {
        log.warn(exception.getMessage());
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }
}

package com.innowise.songapi.controller;

import com.innowise.songapi.dto.ErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException() {
        return getErrorMessageResponseEntity("An unknown error has occured", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException() {
        return getErrorMessageResponseEntity("Illegal argument value", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException() {
        return getErrorMessageResponseEntity("Access denied", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorMessage> handleBadCredentials(BadCredentialsException exception) {
        return getErrorMessageResponseEntity("Incorrect login or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorMessage> handleEntityNotFound(EntityNotFoundException exception) {
        return getErrorMessageResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorMessage> getErrorMessageResponseEntity(String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(
                new ErrorMessage(
                        httpStatus,
                        message,
                        LocalDateTime.now()
                ),
                httpStatus);
    }

}

package com.dudaj.cezspringapp.exception;

import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PatientAlreadyExistsException.class)
    public ResponseEntity<ExceptionDetail> handlePatientAlreadyExists(PatientAlreadyExistsException e) {
        return new ResponseEntity<>(new ExceptionDetail(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ExceptionDetail> handlePatientNotFound(PatientNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDetail(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentExceptionDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<@Nullable String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).toList();

        return new ResponseEntity<>(new MethodArgumentExceptionDetails(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDetail> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = String.format("Parameter '%s' has invalid value '%s'. Expected type: %s",
                                       e.getName(), e.getValue(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");
        return new ResponseEntity<>(new ExceptionDetail(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDetail> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(new ExceptionDetail(message), HttpStatus.BAD_REQUEST);
    }
}
package com.dudaj.cezspringapp.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}

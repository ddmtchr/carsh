package com.brigada.carsh.controller;

import com.brigada.carsh.exception.InconsistentRequestException;
import com.brigada.carsh.exception.NotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InconsistentRequestException.class)
    public ResponseEntity<String> handleInconsistentRequestException(InconsistentRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<String> handleJpaSystemException(JpaSystemException ex) {
        String rm = ExceptionUtils.getRootCauseMessage(ex);
        return new ResponseEntity<>(rm.substring(23, 70), HttpStatus.BAD_REQUEST);
    }
}

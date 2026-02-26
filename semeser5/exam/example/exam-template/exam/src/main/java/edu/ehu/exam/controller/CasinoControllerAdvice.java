package edu.ehu.exam.controller;

import edu.ehu.exam.exception.TableNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CasinoControllerAdvice {

    @ExceptionHandler(TableNotFound.class)
    public ResponseEntity<String> handleException(TableNotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

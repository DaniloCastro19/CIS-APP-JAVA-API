package com.jala.university.api.controllers;

import com.jala.university.core.exception.RequestException;
import com.jala.university.data.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleRuntimeException(RuntimeException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code("P-500")
                .message("Internal server error: "+ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDTO> handleRequestException(RequestException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}

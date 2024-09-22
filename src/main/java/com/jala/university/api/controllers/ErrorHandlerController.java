package com.jala.university.api.controllers;

import com.jala.university.core.exception.BadRequestException;
import com.jala.university.core.exception.NotFoundException;
import com.jala.university.core.exception.RequestException;
import com.jala.university.data.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
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
        log.warn("Request exception: {}", ex.getMessage(), ex);
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        log.warn("Not found exception: {}", ex.getMessage(), ex);
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException(BadRequestException ex) {
        log.warn("Bad request exception: {}", ex.getMessage(), ex);
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
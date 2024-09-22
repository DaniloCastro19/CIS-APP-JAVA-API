package com.jala.university.core.exception;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException{
    private String errorCode;

    public NotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
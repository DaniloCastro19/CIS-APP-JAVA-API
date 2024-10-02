package com.jala.university.core.exception;

import lombok.Data;

@Data
public class BadRequestException extends RuntimeException{
    private String errorCode;

    public BadRequestException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
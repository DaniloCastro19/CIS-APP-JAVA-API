package com.jala.university.core.exception;

import lombok.Data;

@Data
public class RequestException extends RuntimeException {
    private String errorCode;

    public RequestException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
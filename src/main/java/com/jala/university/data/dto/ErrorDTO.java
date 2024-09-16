package com.jala.university.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    private String ErrorCode;
    private String message;
}

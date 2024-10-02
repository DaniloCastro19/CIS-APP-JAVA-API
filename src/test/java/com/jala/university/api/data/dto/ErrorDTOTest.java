package com.jala.university.api.data.dto;

import com.jala.university.data.dto.ErrorDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorDTOTest {

    @Test
    public void testErrorDTOBuilder() {
        String expectedCode = "404";
        String expectedMessage = "Not Found";

        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(expectedCode)
                .message(expectedMessage)
                .build();

        assertEquals(expectedCode, errorDTO.getCode());
        assertEquals(expectedMessage, errorDTO.getMessage());
    }

    @Test
    public void testErrorDTODefaultValues() {
        ErrorDTO errorDTO = ErrorDTO.builder().build();

        assertEquals(null, errorDTO.getCode());
        assertEquals(null, errorDTO.getMessage());
    }

    @Test
    public void testErrorDTOWithNullValues() {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(null)
                .message(null)
                .build();

        assertEquals(null, errorDTO.getCode());
        assertEquals(null, errorDTO.getMessage());
    }
}
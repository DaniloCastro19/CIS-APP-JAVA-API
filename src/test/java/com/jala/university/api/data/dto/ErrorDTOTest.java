package com.jala.university.api.data.dto;

import com.jala.university.data.dto.ErrorDTO;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
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

        assertNull(errorDTO.getCode());
        assertNull(errorDTO.getMessage());
    }

    @Test
    public void testErrorDTOWithNullValues() {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(null)
                .message(null)
                .build();

        assertNull(errorDTO.getCode());
        assertNull(errorDTO.getMessage());
    }

    @Test
    public void testEquals() {
        ErrorDTO errorDTO1 = ErrorDTO.builder()
                .code("404")
                .message("Not Found")
                .build();
        ErrorDTO errorDTO2 = ErrorDTO.builder()
                .code("404")
                .message("Not Found")
                .build();
        ErrorDTO errorDTO3 = ErrorDTO.builder()
                .code("500")
                .message("Internal Server Error")
                .build();

        assertEquals(errorDTO1, errorDTO2);
        assertNotEquals(errorDTO1, errorDTO3);
        assertNotEquals(errorDTO1, null);
        assertNotEquals(errorDTO1, new Object());
    }

    @Test
    public void testHashCode() {
        ErrorDTO errorDTO1 = ErrorDTO.builder()
                .code("404")
                .message("Not Found")
                .build();
        ErrorDTO errorDTO2 = ErrorDTO.builder()
                .code("404")
                .message("Not Found")
                .build();

        assertEquals(errorDTO1.hashCode(), errorDTO2.hashCode());
    }

    @Test
    public void testToString() {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code("404")
                .message("Not Found")
                .build();

        String expectedString = "ErrorDTO(code=404, message=Not Found)";
        assertEquals(expectedString, errorDTO.toString());
    }
}
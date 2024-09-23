package com.jala.university.api.controllers;

import com.jala.university.core.exception.BadRequestException;
import com.jala.university.core.exception.NotFoundException;
import com.jala.university.core.exception.RequestException;
import com.jala.university.data.dto.ErrorDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class ErrorHandlerControllerTest {

    private ErrorHandlerController errorHandler = new ErrorHandlerController();

    @Autowired
    private MockMvc mockMvc;


    @BeforeAll
    static void setUp() {
        System.setProperty("LOG_ROUTE", "src/main/resources/logs/logsApi.log");
        System.setProperty("URL", "jdbc:mysql://localhost:3307/sd3");
        System.setProperty("USERNAME", "root");
        System.setProperty("PASSWORD", "sd5");
        System.setProperty("PORT", "4040");
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Test runtime exception");
        ResponseEntity response = errorHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorDTO);
        ErrorDTO errorDTO = (ErrorDTO) response.getBody();
        assertEquals("P-500", errorDTO.getCode());
        assertEquals("Internal server error: Test runtime exception", errorDTO.getMessage());
    }

    @Test
    void testHandleNullPointerException() {
        NullPointerException ex = new NullPointerException("Test null pointer exception");
        ResponseEntity response = errorHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorDTO);
        ErrorDTO errorDTO = (ErrorDTO) response.getBody();
        assertEquals("P-500", errorDTO.getCode());
        assertEquals("Internal server error: Test null pointer exception", errorDTO.getMessage());
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Test illegal argument exception");
        ResponseEntity response = errorHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorDTO);
        ErrorDTO errorDTO = (ErrorDTO) response.getBody();
        assertEquals("P-500", errorDTO.getCode());
        assertEquals("Internal server error: Test illegal argument exception", errorDTO.getMessage());
    }

    @Test
    void testHandleArrayIndexOutOfBoundsException() {
        ArrayIndexOutOfBoundsException ex = new ArrayIndexOutOfBoundsException("Test array index out of bounds");
        ResponseEntity response = errorHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorDTO);
        ErrorDTO errorDTO = (ErrorDTO) response.getBody();
        assertEquals("P-500", errorDTO.getCode());
        assertEquals("Internal server error: Test array index out of bounds", errorDTO.getMessage());
    }

    @Test
    void testHandleArithmeticException() {
        ArithmeticException ex = new ArithmeticException("Test arithmetic exception");
        ResponseEntity response = errorHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorDTO);
        ErrorDTO errorDTO = (ErrorDTO) response.getBody();
        assertEquals("P-500", errorDTO.getCode());
        assertEquals("Internal server error: Test arithmetic exception", errorDTO.getMessage());
    }

    @Test
    void testHandleRequestException() {
        RequestException ex = new RequestException("P-400", "Custom request error");

        ResponseEntity<ErrorDTO> response = errorHandler.handleRequestException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("P-400", response.getBody().getCode());
        assertEquals("Custom request error", response.getBody().getMessage());
    }

    @Test
    void testHandleNotFoundException() {
        NotFoundException ex = new NotFoundException("P-404", "Resource not found");

        ResponseEntity<Object> response = errorHandler.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("P-404", ((ErrorDTO) response.getBody()).getCode());
        assertEquals("Resource not found", ((ErrorDTO) response.getBody()).getMessage());
    }

    @Test
    void testHandleBadRequestException() {
        BadRequestException ex = new BadRequestException("P-400", "Bad request error");

        ResponseEntity<ErrorDTO> response = errorHandler.handleBadRequestException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()); 
        assertEquals("P-400", response.getBody().getCode());
        assertEquals("Bad request error", response.getBody().getMessage());
    }

}
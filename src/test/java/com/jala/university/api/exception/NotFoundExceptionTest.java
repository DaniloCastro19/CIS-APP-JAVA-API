package com.jala.university.api.exception;

import com.jala.university.core.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class NotFoundExceptionTest {

    @Test
    void testNotFoundExceptionCreation() {
        String expectedCode = "P-404";
        String expectedMessage = "Resource not found";

        NotFoundException exception = new NotFoundException(expectedCode, expectedMessage);

        assertEquals(expectedCode, exception.getErrorCode(), "Error code should match the input code");
        assertEquals(expectedMessage, exception.getMessage(), "Message should match the input message");
    }

    @Test
    void testNotFoundExceptionWithNullValues() {
        NotFoundException exception = new NotFoundException(null, null);

        assertEquals(null, exception.getErrorCode(), "Error code should be null");
        assertEquals(null, exception.getMessage(), "Message should be null");
    }

    @Test
    void testNotFoundExceptionWithEmptyMessage() {
        String expectedCode = "P-404";
        String expectedMessage = "";

        NotFoundException exception = new NotFoundException(expectedCode, expectedMessage);

        assertEquals(expectedCode, exception.getErrorCode(), "Error code should match the input code");
        assertEquals(expectedMessage, exception.getMessage(), "Message should match the input message");
    }

    @Test
    void testNotFoundExceptionToString() {
        String expectedCode = "P-404";
        String expectedMessage = "Resource not found";

        NotFoundException exception = new NotFoundException(expectedCode, expectedMessage);
        String expectedString = "NotFoundException(errorCode=P-404)";

        assertEquals(expectedString, exception.toString(), "toString should return expected string format");
    }

    @Test
    void testNotFoundExceptionEquals() {
        NotFoundException exception1 = new NotFoundException("P-404", "Resource not found");
        NotFoundException exception2 = new NotFoundException("P-404", "Resource not found");
        NotFoundException exception3 = new NotFoundException("P-500", "Different error");

        assertEquals(exception1, exception2, "Exceptions with the same code and message should be equal");
        assertNotEquals(exception1, exception3, "Exceptions with different codes or messages should not be equal");
    }

    @Test
    void testNotFoundExceptionHashCode() {
        NotFoundException exception1 = new NotFoundException("P-404", "Resource not found");
        NotFoundException exception2 = new NotFoundException("P-404", "Resource not found");

        assertEquals(exception1.hashCode(), exception2.hashCode(), "Hash codes should match for equal exceptions");
    }

    @Test
    void testNotFoundExceptionSetErrorCode() {
        NotFoundException exception = new NotFoundException("P-404", "Resource not found");
        exception.setErrorCode("P-400");

        assertEquals("P-400", exception.getErrorCode(), "Error code should be updated");
    }

    @Test
    void testNotFoundExceptionCanEqual() {
        NotFoundException exception = new NotFoundException("P-404", "Resource not found");

        // Test with a valid object
        assertEquals(true, exception.equals(new NotFoundException("P-404", "Resource not found")), "canEqual should return true for NotFoundException");

        // Test with an invalid object
        assertEquals(false, exception.equals(new Object()), "canEqual should return false for non-NotFoundException objects");
    }
}

package com.jala.university.api.exception;

import com.jala.university.core.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BadRequestExceptionTest {

    @Test
    void testBadRequestExceptionConstructor() {
        String expectedCode = "P-400";
        String expectedMessage = "Bad request error";

        BadRequestException exception = new BadRequestException(expectedCode, expectedMessage);

        assertEquals(expectedCode, exception.getErrorCode(), "Error code should match the input code");
        assertEquals(expectedMessage, exception.getMessage(), "Message should match the input message");
    }

    @Test
    void testBadRequestExceptionInheritance() {
        BadRequestException exception = new BadRequestException("P-400", "Bad request error");

        assertEquals("Bad request error", exception.getMessage(), "Should inherit message from RuntimeException");
        assertEquals(RuntimeException.class, exception.getClass().getSuperclass(), "Should extend RuntimeException");
    }

    @Test
    void testBadRequestExceptionWithNullValues() {
        BadRequestException exception = new BadRequestException(null, null);

        assertEquals(null, exception.getErrorCode(), "Error code should be null");
        assertEquals(null, exception.getMessage(), "Message should be null");
    }

    @Test
    void testBadRequestExceptionWithEmptyMessage() {
        String expectedCode = "P-400";
        String expectedMessage = "";

        BadRequestException exception = new BadRequestException(expectedCode, expectedMessage);

        assertEquals(expectedCode, exception.getErrorCode(), "Error code should match the input code");
        assertEquals(expectedMessage, exception.getMessage(), "Message should match the input message");
    }

    @Test
    void testBadRequestExceptionToString() {
        String expectedCode = "P-400";
        String expectedMessage = "Bad request error";

        BadRequestException exception = new BadRequestException(expectedCode, expectedMessage);
        String expectedString = "BadRequestException(errorCode=P-400)";

        assertEquals(expectedString, exception.toString(), "toString should return expected string format");
    }

    @Test
    void testBadRequestExceptionEquals() {
        BadRequestException exception1 = new BadRequestException("P-400", "Bad request error");
        BadRequestException exception2 = new BadRequestException("P-400", "Bad request error");
        BadRequestException exception3 = new BadRequestException("P-500", "Different error");

        assertEquals(exception1, exception2, "Exceptions with the same code and message should be equal");
        assertNotEquals(exception1, exception3, "Exceptions with different codes or messages should not be equal");
    }

    @Test
    void testBadRequestExceptionHashCode() {
        BadRequestException exception1 = new BadRequestException("P-400", "Bad request error");
        BadRequestException exception2 = new BadRequestException("P-400", "Bad request error");

        assertEquals(exception1.hashCode(), exception2.hashCode(), "Hash codes should match for equal exceptions");
    }

    @Test
    void testBadRequestExceptionSetErrorCode() {
        BadRequestException exception = new BadRequestException("P-400", "Bad request error");
        exception.setErrorCode("P-500");

        assertEquals("P-500", exception.getErrorCode(), "Error code should be updated");
    }

    @Test
    void testBadRequestExceptionCanEqual() {
        BadRequestException exception = new BadRequestException("P-400", "Bad request error");

        // Test with a valid object
        assertEquals(true, exception.equals(new BadRequestException("P-400", "Bad request error")), "canEqual should return true for BadRequestException");

        // Test with an invalid object
        assertEquals(false, exception.equals(new Object()), "canEqual should return false for non-BadRequestException objects");
    }
}

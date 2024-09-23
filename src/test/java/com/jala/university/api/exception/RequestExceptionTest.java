package com.jala.university.api.exception;


import com.jala.university.core.exception.RequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestExceptionTest {
    @Test
    void testEquals() {
        RequestException exception1 = new RequestException("P-400", "Error");
        RequestException exception2 = new RequestException("P-400", "Error");
        RequestException exception3 = new RequestException("P-500", "Different error");

        assertEquals(exception1, exception2, "Exceptions with same error code and message should be equal");
        assertNotEquals(exception1, exception3, "Exceptions with different error codes should not be equal");
        assertNotEquals(exception1, null, "Should not be equal to null");
        assertNotEquals(exception1, "Not an exception", "Should not be equal to a different type");
    }

    @Test
    void testHashCode() {
        RequestException exception1 = new RequestException("P-400", "Error");
        RequestException exception2 = new RequestException("P-400", "Error");
        RequestException exception3 = new RequestException("P-500", "Different error");

        assertEquals(exception1.hashCode(), exception2.hashCode(), "Hash codes should match for equal exceptions");
        assertNotEquals(exception1.hashCode(), exception3.hashCode(), "Hash codes should not match for different exceptions");
    }

    @Test
    void testSetErrorCode() {
        RequestException exception = new RequestException("P-400", "Error");
        exception.setErrorCode("P-404");
        assertEquals("P-404", exception.getErrorCode(), "Error code should be updated");
    }

    @Test
    void testToString() {
        RequestException exception = new RequestException("P-400", "Error occurred");
        String expectedString = "RequestException(errorCode=P-400)";
        assertEquals(expectedString, exception.toString(), "toString should return expected string format");
    }

    @Test
    void testCanEqual() {
        RequestException exception = new RequestException("P-400", "Error");
        assertTrue(exception.equals(new RequestException("P-400", "Another error")),
                "canEqual should return true for the same type");
        assertFalse(exception.equals(new Object()), "canEqual should return false for different types");
    }
}
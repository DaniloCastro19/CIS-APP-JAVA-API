package com.jala.university.api.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jala.university.core.security.PassEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class PassEncoderTest {

    @Test
    public void testPasswordEncoder() {
        PassEncoder passEncoder = new PassEncoder();

        PasswordEncoder encoder = passEncoder.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder.matches("password", encoder.encode("password")));
    }
}
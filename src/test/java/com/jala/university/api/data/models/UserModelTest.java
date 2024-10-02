package com.jala.university.api.data.models;

import com.jala.university.data.models.UserModel;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserModelTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUserModel() {
        UserModel shark = new UserModel();
        shark.setName("Baby Shark");
        shark.setLogin("shark");
        shark.setPassword("securePassword");

        Set<ConstraintViolation<UserModel>> violations = validator.validate(shark);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUserModelNameValidation() {
        UserModel shark = new UserModel();
        shark.setName("");
        shark.setLogin("shark");
        shark.setPassword("securePassword");

        Set<ConstraintViolation<UserModel>> violations = validator.validate(shark);
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testUserModelLoginSizeValidation() {
        UserModel shark = new UserModel();
        shark.setName("Baby Shark");
        shark.setLogin("thisLoginIsTooLong");
        shark.setPassword("securePassword");

        Set<ConstraintViolation<UserModel>> violations = validator.validate(shark);
        assertEquals(1, violations.size());
        assertEquals("Login cannot be more than 16 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void testUserModelPasswordValidation() {
        UserModel shark = new UserModel();
        shark.setName("Baby Shark");
        shark.setLogin("shark");
        shark.setPassword("");

        Set<ConstraintViolation<UserModel>> violations = validator.validate(shark);
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    public void testUserModelNameSizeValidation() {
        UserModel shark = new UserModel();
        shark.setName("This name is way too long for validation purpose");
        shark.setLogin("shark");
        shark.setPassword("securePassword");

        Set<ConstraintViolation<UserModel>> violations = validator.validate(shark);
        assertEquals(1, violations.size());
        assertEquals("Name cannot be more than 30 characters", violations.iterator().next().getMessage());
    }
}
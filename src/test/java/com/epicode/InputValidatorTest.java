package com.epicode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InputValidatorTest {

    @Test
    public void testSanitizeNameValid() {
        assertEquals("John", InputValidator.sanitizeName("  John "));
    }

    @Test
    public void testSanitizeNameInvalid() {
        assertThrows(InputValidationException.class, () -> InputValidator.sanitizeName(null));
        assertThrows(InputValidationException.class, () -> InputValidator.sanitizeName("J1"));
        assertThrows(InputValidationException.class, () -> InputValidator.sanitizeName("A"));
    }

    @Test
    public void testSanitizeIdValid() {
        assertEquals("ABC123", InputValidator.sanitizeId("  ABC-123  "));
    }

    @Test
    public void testSanitizeIdInvalid() {
        assertThrows(InputValidationException.class, () -> InputValidator.sanitizeId(null));
        assertThrows(InputValidationException.class, () -> InputValidator.sanitizeId("   "));
    }
}
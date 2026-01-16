package com.epicode;

/**
 * Utility class for input validation and sanitization.
 * Static methods to sanitize names and bike IDs according to rules.
 */
@Secured("Validates and sanitizes all user input")
@RoleType("Validator")
public final class InputValidator {

    /**
     * Sanitizes and validates a name.
     * @param input name string
     * @return sanitized name
     * @throws InputValidationException if name is invalid
     */
    @Sanitized
    public static String sanitizeName(String input) {
        if (input == null) {
            throw new InputValidationException("Name cannot be null");
        }
        input = input.trim();
        if (!input.matches("[a-zA-Z ]{2,}")) {
            throw new InputValidationException("Name must be at least 2 letters and contain only letters");
        }
        return input;
    }

    /**
     * Sanitizes and validates a bike ID.
     * @param input bike ID string
     * @return sanitized bike ID
     * @throws InputValidationException if ID is invalid
     */
    @Sanitized
    public static String sanitizeId(String input) {
        if (input == null || input.isBlank()) {
            throw new InputValidationException("Bike ID cannot be null or blank");
        }
        return input.replaceAll("[^a-zA-Z0-9]", "").trim();
    }
}
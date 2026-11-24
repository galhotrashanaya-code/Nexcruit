package utils;

/**
 * Simple helper class to validate user inputs using basic Java checks.
 * We avoid complex regex to keep the logic easy to explain in viva.
 * Concept used: Static Methods and Utility Class.
 */
public final class InputValidator {

    // Private constructor so no one creates an object of this utility class
    private InputValidator() {
    }

    // Checks if the string is not null and not blank
    public static boolean isNonEmpty(String text) {
        if (text == null) {
            return false;
        }
        return !text.trim().isEmpty();
    }

    // Simple email check using contains("@") and contains(".")
    public static boolean isValidEmail(String email) {
        if (!isNonEmpty(email)) {
            return false;
        }
        String clean = email.trim();
        // Basic beginner-friendly check: must have @ and . with some text around them
        int atIndex = clean.indexOf('@');
        int dotIndex = clean.lastIndexOf('.');
        return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < clean.length() - 1;
    }

    // Checks if CGPA is in the realistic college range (0.0 to 10.0)
    public static boolean isValidCgpa(double cgpa) {
        return cgpa >= 0.0 && cgpa <= 10.0;
    }

    // Checks if experience years are non-negative
    public static boolean isValidExperience(int years) {
        return years >= 0 && years <= 50;
    }
}

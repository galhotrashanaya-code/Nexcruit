package utils;

import java.util.Scanner;

/**
 * Helper class for terminal UI printing and beginner-friendly, crash-proof Scanner input.
 * Handles invalid numbers and blank inputs with simple while-loops and try-catch blocks.
 * Concept used: Scanner, Exception Handling (NumberFormatException), Static Methods.
 */
public final class ConsoleUI {

    private ConsoleUI() {
    }

    public static void printBanner() {
        System.out.println("================================================================================");
        System.out.println("   _  _ _____  ____ ____  _   _ ___ _____ ");
        System.out.println("  | \\| | ____|/ ___|  _ \\| | | |_ _|_   _|");
        System.out.println("  | .` |  _| | |   | |_) | | | || |  | |  ");
        System.out.println("  |_|\\_|_____|\\____|_| \\_\\\\___/|___| |_|  ");
        System.out.println("       Smart Recruitment & Applicant Tracking System");
        System.out.println("================================================================================");
    }

    public static void printHeader(String title) {
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("  >> " + title.toUpperCase());
        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void printDivider() {
        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void printSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void pressEnterToContinue(Scanner scanner) {
        System.out.print("\nPress [ENTER] to continue...");
        try {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        } catch (Exception ignored) {
        }
    }

    // Reads an integer safely without crashing if user types letters
    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return min;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a number between " + min + " and " + max + ".");
                continue;
            }
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter digits only.");
            }
        }
    }

    // Reads a double safely (e.g. for CGPA) without crashing
    public static double readDouble(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return min;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid decimal number.");
                continue;
            }
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("Value must be between %.2f and %.2f.%n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal number. Example: 8.5");
            }
        }
    }

    // Reads a string, with optional required check
    public static String readString(Scanner scanner, String prompt, boolean required) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return "";
            }
            String input = scanner.nextLine().trim();
            if (required && input.isEmpty()) {
                System.out.println("This field is required. It cannot be empty.");
                continue;
            }
            return input;
        }
    }

    // Reads and validates an email address
    public static String readEmail(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return "";
            }
            String input = scanner.nextLine().trim();
            if (InputValidator.isValidEmail(input)) {
                return input;
            } else {
                System.out.println("Invalid email format (e.g. user@gmail.com). Please try again.");
            }
        }
    }
}

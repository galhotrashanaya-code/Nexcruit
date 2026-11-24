package auth;

import java.io.Serializable;

/**
 * Abstract base class representing a generic user in the Nexcruit system.
 * Concepts used:
 * - Abstraction (cannot be instantiated directly)
 * - Encapsulation (private variables with getters and setters)
 * - Inheritance (parent class for Candidate, Recruiter, and Admin)
 * - Serializable (allows saving user objects to file)
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Private fields for encapsulation
    private String userId;
    private String name;
    private String email;
    private String password;
    private String role; // "CANDIDATE", "RECRUITER", or "ADMIN"

    // Constructor to initialize common user data
    public User(String userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Abstract method: Every subclass must implement its own way of showing profile details.
    // Concept used: Polymorphism
    public abstract void displayProfile();

    // Helper method to verify passwords during login
    public boolean checkPassword(String inputPassword) {
        if (this.password == null || inputPassword == null) {
            return false;
        }
        return this.password.equals(inputPassword);
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "[" + role + "] ID: " + userId + " | Name: " + name + " | Email: " + email;
    }
}

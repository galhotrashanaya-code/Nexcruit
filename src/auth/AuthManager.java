package auth;

import exceptions.InvalidInputException;
import exceptions.UserNotFoundException;
import utils.InputValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Manages user accounts, registrations, and logins.
 * Concepts used:
 * - HashMap (for fast O(1) user lookup by email and by ID)
 * - ArrayList (to return lists of candidates or recruiters)
 * - Custom Exceptions (InvalidInputException, UserNotFoundException)
 * - Simple if-else logic and loops
 */
public class AuthManager implements Serializable {
    private static final long serialVersionUID = 1L;

    // Fast lookup maps: key is lowercase email, value is User object
    private HashMap<String, User> usersByEmail;
    // Fast lookup maps: key is user ID (e.g. CAN-101), value is User object
    private HashMap<String, User> usersById;

    // Simple counters to auto-generate unique user IDs
    private int candidateCounter;
    private int recruiterCounter;
    private int adminCounter;

    public AuthManager() {
        this.usersByEmail = new HashMap<String, User>();
        this.usersById = new HashMap<String, User>();
        this.candidateCounter = 101;
        this.recruiterCounter = 101;
        this.adminCounter = 101;
    }

    // Common input validation method for all registrations
    private void validateCommon(String name, String email, String password) throws InvalidInputException {
        if (!InputValidator.isNonEmpty(name)) {
            throw new InvalidInputException("Name cannot be empty.");
        }
        if (!InputValidator.isValidEmail(email)) {
            throw new InvalidInputException("Invalid email format provided: " + email);
        }
        if (!InputValidator.isNonEmpty(password) || password.length() < 4) {
            throw new InvalidInputException("Password must be at least 4 characters long.");
        }
        if (usersByEmail.containsKey(email.trim().toLowerCase())) {
            throw new InvalidInputException("An account with email " + email + " already exists.");
        }
    }

    // Helper to store user in both lookup maps
    private void saveUser(User user) {
        usersByEmail.put(user.getEmail().toLowerCase(), user);
        usersById.put(user.getUserId(), user);
    }

    // Registers a new candidate with full details
    public Candidate registerCandidate(String name, String email, String password,
                                       String phone, String education, double cgpa,
                                       int experienceYears, ArrayList<String> skills)
            throws InvalidInputException {

        validateCommon(name, email, password);

        if (!InputValidator.isValidCgpa(cgpa)) {
            throw new InvalidInputException("CGPA must be between 0.0 and 10.0.");
        }
        if (!InputValidator.isValidExperience(experienceYears)) {
            throw new InvalidInputException("Experience must be between 0 and 50 years.");
        }

        String userId = "CAN-" + candidateCounter;
        candidateCounter++;

        Candidate candidate = new Candidate(userId, name.trim(), email.trim().toLowerCase(),
                password, phone, education, cgpa, experienceYears, skills);

        saveUser(candidate);
        return candidate;
    }

    // Overloaded registerCandidate method for basic profile creation
    public Candidate registerCandidate(String name, String email, String password, double cgpa)
            throws InvalidInputException {
        return registerCandidate(name, email, password, "", "Undergraduate", cgpa, 0, new ArrayList<String>());
    }

    // Registers a new recruiter
    public Recruiter registerRecruiter(String name, String email, String password,
                                       String companyName, String department, String contactNumber)
            throws InvalidInputException {

        validateCommon(name, email, password);

        if (!InputValidator.isNonEmpty(companyName)) {
            throw new InvalidInputException("Company name cannot be blank.");
        }

        String userId = "REC-" + recruiterCounter;
        recruiterCounter++;

        Recruiter recruiter = new Recruiter(userId, name.trim(), email.trim().toLowerCase(),
                password, companyName.trim(), department.trim(), contactNumber);

        saveUser(recruiter);
        return recruiter;
    }

    // Registers an administrator
    public Admin registerAdmin(String name, String email, String password)
            throws InvalidInputException {

        validateCommon(name, email, password);

        String userId = "ADM-" + adminCounter;
        adminCounter++;

        Admin admin = new Admin(userId, name.trim(), email.trim().toLowerCase(), password);
        saveUser(admin);
        return admin;
    }

    // Authenticates a user with email and password
    public User login(String email, String password) throws UserNotFoundException, InvalidInputException {
        if (!InputValidator.isNonEmpty(email) || !InputValidator.isNonEmpty(password)) {
            throw new InvalidInputException("Email and password are required.");
        }
        String cleanEmail = email.trim().toLowerCase();
        User user = usersByEmail.get(cleanEmail);

        if (user == null) {
            throw new UserNotFoundException("No account found with email: " + email);
        }
        if (!user.checkPassword(password)) {
            throw new UserNotFoundException("Incorrect password for account: " + email);
        }
        return user;
    }

    // Get user by unique ID
    public User getUserById(String userId) {
        if (userId == null) return null;
        return usersById.get(userId.trim().toUpperCase());
    }

    // Check if an email is already registered
    public boolean isEmailRegistered(String email) {
        if (email == null) return false;
        return usersByEmail.containsKey(email.trim().toLowerCase());
    }

    // Returns a list of all users
    public ArrayList<User> getAllUsers() {
        return new ArrayList<User>(usersById.values());
    }

    // Returns a list of candidates only using simple instanceof check
    public ArrayList<Candidate> getCandidates() {
        ArrayList<Candidate> list = new ArrayList<Candidate>();
        for (User u : usersById.values()) {
            if (u instanceof Candidate) {
                list.add((Candidate) u);
            }
        }
        return list;
    }

    // Returns a list of recruiters only using simple instanceof check
    public ArrayList<Recruiter> getRecruiters() {
        ArrayList<Recruiter> list = new ArrayList<Recruiter>();
        for (User u : usersById.values()) {
            if (u instanceof Recruiter) {
                list.add((Recruiter) u);
            }
        }
        return list;
    }

    // Restores users from file and keeps ID counters in sync
    public void restoreUsers(Collection<User> loadedUsers) {
        usersByEmail.clear();
        usersById.clear();
        if (loadedUsers == null) return;

        for (User u : loadedUsers) {
            saveUser(u);
            try {
                String[] parts = u.getUserId().split("-");
                if (parts.length == 2) {
                    int idNum = Integer.parseInt(parts[1]);
                    if (u instanceof Candidate && idNum >= candidateCounter) {
                        candidateCounter = idNum + 1;
                    } else if (u instanceof Recruiter && idNum >= recruiterCounter) {
                        recruiterCounter = idNum + 1;
                    } else if (u instanceof Admin && idNum >= adminCounter) {
                        adminCounter = idNum + 1;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
}

package auth;

/**
 * Subclass representing a System Administrator.
 * Has root privileges to view all users, recruiters, and platform statistics.
 * Concepts used:
 * - Inheritance (extends User)
 * - Method Overriding (implements displayProfile())
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password, "ADMIN");
    }

    @Override
    public void displayProfile() {
        System.out.println("----------------- ADMIN PROFILE -----------------");
        System.out.println("Admin ID    : " + getUserId());
        System.out.println("Name        : " + getName());
        System.out.println("Email       : " + getEmail());
        System.out.println("Role        : System Administrator (Full System Visibility)");
        System.out.println("-------------------------------------------------");
    }
}

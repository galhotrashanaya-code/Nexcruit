package auth;

/**
 * Subclass representing a Corporate Recruiter or Hiring Manager.
 * Concepts used:
 * - Inheritance (extends User)
 * - Method Overriding (implements displayProfile())
 */
public class Recruiter extends User {
    private static final long serialVersionUID = 1L;

    private String companyName;
    private String department;
    private String contactNumber;

    public Recruiter(String userId, String name, String email, String password,
                     String companyName, String department, String contactNumber) {
        super(userId, name, email, password, "RECRUITER");
        this.companyName = companyName;
        this.department = department;
        this.contactNumber = contactNumber;
    }

    @Override
    public void displayProfile() {
        System.out.println("----------------- RECRUITER PROFILE -----------------");
        System.out.println("Recruiter ID   : " + getUserId());
        System.out.println("Full Name      : " + getName());
        System.out.println("Work Email     : " + getEmail());
        System.out.println("Company Name   : " + companyName);
        System.out.println("Department     : " + department);
        System.out.println("Contact Number : " + (contactNumber != null ? contactNumber : "Not provided"));
        System.out.println("-----------------------------------------------------");
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}

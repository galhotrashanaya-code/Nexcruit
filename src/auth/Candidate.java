package auth;

import java.util.ArrayList;

/**
 * Subclass representing a Job Candidate (Applicant).
 * Inherits common attributes from User and adds academic and technical skill details.
 * Concepts used:
 * - Inheritance (extends User)
 * - Method Overriding (implements displayProfile())
 * - Method Overloading (multiple updateProfile() and constructor variants)
 * - ArrayList (stores list of technical skills)
 */
public class Candidate extends User {
    private static final long serialVersionUID = 1L;

    // Candidate-specific attributes
    private String phone;
    private String education;
    private double cgpa;
    private int experienceYears;
    private ArrayList<String> skills;

    // Constructor without pre-existing skills list
    public Candidate(String userId, String name, String email, String password,
                     String phone, String education, double cgpa, int experienceYears) {
        super(userId, name, email, password, "CANDIDATE");
        this.phone = phone;
        this.education = education;
        this.cgpa = cgpa;
        this.experienceYears = experienceYears;
        this.skills = new ArrayList<String>();
    }

    // Overloaded Constructor with pre-existing skills list
    public Candidate(String userId, String name, String email, String password,
                     String phone, String education, double cgpa, int experienceYears,
                     ArrayList<String> skills) {
        super(userId, name, email, password, "CANDIDATE");
        this.phone = phone;
        this.education = education;
        this.cgpa = cgpa;
        this.experienceYears = experienceYears;
        if (skills != null) {
            this.skills = new ArrayList<String>(skills);
        } else {
            this.skills = new ArrayList<String>();
        }
    }

    // Overriding abstract method from User class
    @Override
    public void displayProfile() {
        System.out.println("----------------- CANDIDATE PROFILE -----------------");
        System.out.println("Candidate ID     : " + getUserId());
        System.out.println("Full Name        : " + getName());
        System.out.println("Email Address    : " + getEmail());
        System.out.println("Phone Number     : " + (phone != null && !phone.isEmpty() ? phone : "Not specified"));
        System.out.println("Education/Degree : " + (education != null && !education.isEmpty() ? education : "Not specified"));
        System.out.printf("Cumulative CGPA  : %.2f / 10.0%n", cgpa);
        System.out.println("Experience       : " + experienceYears + " year(s)");
        System.out.print("Technical Skills : ");
        if (skills == null || skills.isEmpty()) {
            System.out.println("No skills added yet");
        } else {
            System.out.println(String.join(", ", skills));
        }
        System.out.println("-----------------------------------------------------");
    }

    // Method Overloading: Update basic profile details
    public void updateProfile(String name, String phone, String education, double cgpa, int experienceYears) {
        setName(name);
        this.phone = phone;
        this.education = education;
        this.cgpa = cgpa;
        this.experienceYears = experienceYears;
    }

    // Method Overloading: Update profile details including skills list
    public void updateProfile(String name, String phone, String education, double cgpa, int experienceYears, ArrayList<String> skills) {
        updateProfile(name, phone, education, cgpa, experienceYears);
        if (skills != null) {
            this.skills = new ArrayList<String>(skills);
        } else {
            this.skills = new ArrayList<String>();
        }
    }

    // Adds a skill avoiding duplicates using simple case-insensitive comparison
    public void addSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return;
        }
        String cleanSkill = skill.trim();
        for (String existing : skills) {
            if (existing.equalsIgnoreCase(cleanSkill)) {
                return; // Skill already present
            }
        }
        skills.add(cleanSkill);
    }

    // Getters and Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public ArrayList<String> getSkills() {
        return new ArrayList<String>(skills);
    }

    public void setSkills(ArrayList<String> skills) {
        if (skills != null) {
            this.skills = new ArrayList<String>(skills);
        } else {
            this.skills = new ArrayList<String>();
        }
    }
}

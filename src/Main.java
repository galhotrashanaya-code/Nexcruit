import auth.Admin;
import auth.AuthManager;
import auth.Candidate;
import auth.Recruiter;
import auth.User;
import applications.Application;
import applications.ApplicationManager;
import exceptions.DuplicateApplicationException;
import exceptions.InvalidInputException;
import exceptions.UserNotFoundException;
import interviews.Interview;
import interviews.InterviewManager;
import interviews.InterviewRound;
import interviews.InterviewStatus;
import jobs.Job;
import jobs.JobManager;
import matching.CandidateMatchResult;
import matching.MatchEngine;
import reports.ReportManager;
import storage.DataStore;
import storage.FileManager;
import utils.ApplicationStatus;
import utils.ConsoleUI;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main Controller for the NEXCRUIT Console Application.
 * Handles the main menu, candidate portal, recruiter portal, and admin portal.
 * Concepts used:
 * - OOP Principles (Polymorphism, Abstraction, Inheritance)
 * - Control flow: while loops, switch-case, if-else
 * - ArrayList collections
 * - File Persistence and Serialization
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);

    // Core Managers
    private static AuthManager authManager;
    private static JobManager jobManager;
    private static ApplicationManager applicationManager;
    private static InterviewManager interviewManager;
    private static MatchEngine matchEngine;
    private static ReportManager reportManager;
    private static FileManager fileManager;

    // Currently logged-in user session
    private static User currentUser = null;

    public static void main(String[] args) {
        // Initialize managers and load saved data
        initializeApplication();

        ConsoleUI.printBanner();
        System.out.println(" Welcome to Nexcruit - Smart Recruitment Management System");

        boolean running = true;
        while (running) {
            try {
                if (currentUser == null) {
                    running = showMainMenu();
                } else if (currentUser instanceof Candidate) {
                    showCandidateMenu((Candidate) currentUser);
                } else if (currentUser instanceof Recruiter) {
                    showRecruiterMenu((Recruiter) currentUser);
                } else if (currentUser instanceof Admin) {
                    showAdminMenu((Admin) currentUser);
                }
            } catch (Exception e) {
                // Catch any unexpected input error and continue gracefully
                System.out.println("Notice: " + e.getMessage());
                break;
            }
        }

        // Save data before closing
        saveApplicationState();
        System.out.println("\nThank you for using Nexcruit. Goodbye!");
    }

    // Loads data from file into managers
    private static void initializeApplication() {
        authManager = new AuthManager();
        jobManager = new JobManager();
        applicationManager = new ApplicationManager();
        interviewManager = new InterviewManager();
        matchEngine = new MatchEngine();
        fileManager = new FileManager();

        DataStore store = fileManager.loadData();
        authManager.restoreUsers(store.getUsers());
        jobManager.restoreJobs(store.getJobs());
        applicationManager.restoreApplications(store.getApplications());
        interviewManager.restoreInterviews(store.getInterviews());

        reportManager = new ReportManager(authManager, jobManager, applicationManager, interviewManager);
    }

    // Saves current state to disk
    private static void saveApplicationState() {
        boolean saved = fileManager.saveData(authManager, jobManager, applicationManager, interviewManager);
        if (saved) {
            ConsoleUI.printInfo("Application state saved successfully.");
        }
    }

    // =========================================================================
    // MAIN AUTHENTICATION MENU
    // =========================================================================

    private static boolean showMainMenu() {
        ConsoleUI.printHeader("Main Menu");
        System.out.println(" 1. Register New Account");
        System.out.println(" 2. Login to Account");
        System.out.println(" 3. Exit Application");
        ConsoleUI.printDivider();

        int choice = ConsoleUI.readInt(scanner, "Enter your choice [1-3]: ", 1, 3);
        switch (choice) {
            case 1:
                handleRegistration();
                break;
            case 2:
                handleLogin();
                break;
            case 3:
                return false;
        }
        return true;
    }

    private static void handleRegistration() {
        ConsoleUI.printHeader("User Registration");
        System.out.println("Select Account Type:");
        System.out.println(" 1. Candidate (Job Seeker)");
        System.out.println(" 2. Recruiter (Hiring Manager)");
        System.out.println(" 3. Back to Main Menu");
        ConsoleUI.printDivider();

        int roleChoice = ConsoleUI.readInt(scanner, "Choose role [1-3]: ", 1, 3);
        if (roleChoice == 3) return;

        String name = ConsoleUI.readString(scanner, "Enter Full Name: ", true);
        String email = ConsoleUI.readEmail(scanner, "Enter Email Address: ");
        String password = ConsoleUI.readString(scanner, "Enter Password (min 4 chars): ", true);

        try {
            if (roleChoice == 1) {
                String phone = ConsoleUI.readString(scanner, "Enter Phone Number: ", false);
                String education = ConsoleUI.readString(scanner, "Enter Highest Qualification (e.g. B.Tech CS): ", true);
                double cgpa = ConsoleUI.readDouble(scanner, "Enter Cumulative CGPA [0.0 - 10.0]: ", 0.0, 10.0);
                int expYears = ConsoleUI.readInt(scanner, "Enter Years of Experience [0 - 50]: ", 0, 50);

                System.out.print("Enter Technical Skills (comma-separated, e.g. Java, SQL, Git): ");
                String skillsInput = scanner.nextLine();
                ArrayList<String> skills = parseCommaSeparated(skillsInput);

                Candidate candidate = authManager.registerCandidate(name, email, password, phone, education, cgpa, expYears, skills);
                saveApplicationState();
                ConsoleUI.printSuccess("Candidate registered successfully! Your ID is: " + candidate.getUserId());
                ConsoleUI.printInfo("You can now login using your email and password.");

            } else if (roleChoice == 2) {
                String company = ConsoleUI.readString(scanner, "Enter Company Name: ", true);
                String dept = ConsoleUI.readString(scanner, "Enter Department (e.g. Engineering Talent): ", true);
                String contact = ConsoleUI.readString(scanner, "Enter Contact Phone: ", false);

                Recruiter recruiter = authManager.registerRecruiter(name, email, password, company, dept, contact);
                saveApplicationState();
                ConsoleUI.printSuccess("Recruiter registered successfully! Your ID is: " + recruiter.getUserId());
                ConsoleUI.printInfo("You can now login using your work email and password.");
            }
        } catch (InvalidInputException e) {
            ConsoleUI.printError("Registration failed: " + e.getMessage());
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleLogin() {
        ConsoleUI.printHeader("User Login");
        String email = ConsoleUI.readEmail(scanner, "Enter Registered Email: ");
        String password = ConsoleUI.readString(scanner, "Enter Password: ", true);

        try {
            currentUser = authManager.login(email, password);
            ConsoleUI.printSuccess("Welcome, " + currentUser.getName() + " [" + currentUser.getRole() + "]!");
        } catch (UserNotFoundException e) {
            ConsoleUI.printError("Login failed: " + e.getMessage());
        } catch (InvalidInputException e) {
            ConsoleUI.printError("Input error: " + e.getMessage());
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    // =========================================================================
    // CANDIDATE WORKFLOW MENU
    // =========================================================================

    private static void showCandidateMenu(Candidate candidate) {
        ConsoleUI.printHeader("Candidate Portal | " + candidate.getName() + " (" + candidate.getUserId() + ")");
        System.out.println(" 1. View Profile");
        System.out.println(" 2. Update Profile");
        System.out.println(" 3. Browse Jobs");
        System.out.println(" 4. Apply for Job");
        System.out.println(" 5. Track Applications & Interviews");
        System.out.println(" 6. Logout");
        ConsoleUI.printDivider();

        int choice = ConsoleUI.readInt(scanner, "Enter your choice [1-6]: ", 1, 6);
        switch (choice) {
            case 1:
                candidate.displayProfile();
                ConsoleUI.pressEnterToContinue(scanner);
                break;
            case 2:
                handleCandidateProfileUpdate(candidate);
                break;
            case 3:
                handleBrowseJobs(candidate);
                break;
            case 4:
                handleCandidateApply(candidate);
                break;
            case 5:
                handleTrackApplications(candidate);
                break;
            case 6:
                currentUser = null;
                ConsoleUI.printInfo("Logged out successfully.");
                break;
        }
    }

    private static void handleCandidateProfileUpdate(Candidate candidate) {
        ConsoleUI.printHeader("Update Candidate Profile");

        System.out.println("Current Name: " + candidate.getName());
        String name = ConsoleUI.readString(scanner, "Enter New Name (leave blank to keep current): ", false);
        if (name.isEmpty()) name = candidate.getName();

        System.out.println("Current Phone: " + candidate.getPhone());
        String phone = ConsoleUI.readString(scanner, "Enter New Phone (leave blank to keep current): ", false);
        if (phone.isEmpty()) phone = candidate.getPhone();

        System.out.println("Current Education: " + candidate.getEducation());
        String edu = ConsoleUI.readString(scanner, "Enter New Education (leave blank to keep current): ", false);
        if (edu.isEmpty()) edu = candidate.getEducation();

        System.out.printf("Current CGPA: %.2f%n", candidate.getCgpa());
        double cgpa = ConsoleUI.readDouble(scanner, "Enter New CGPA [0.0 - 10.0]: ", 0.0, 10.0);

        System.out.println("Current Experience: " + candidate.getExperienceYears() + " year(s)");
        int exp = ConsoleUI.readInt(scanner, "Enter New Experience [0 - 50]: ", 0, 50);

        System.out.println("Current Skills: " + String.join(", ", candidate.getSkills()));
        System.out.print("Enter New Skills (comma-separated, leave blank to keep current): ");
        String skillsLine = scanner.nextLine();
        ArrayList<String> skills;
        if (skillsLine.trim().isEmpty()) {
            skills = candidate.getSkills();
        } else {
            skills = parseCommaSeparated(skillsLine);
        }

        candidate.updateProfile(name, phone, edu, cgpa, exp, skills);
        saveApplicationState();
        ConsoleUI.printSuccess("Profile updated successfully!");
        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleBrowseJobs(Candidate candidate) {
        ConsoleUI.printHeader("Available Job Openings");
        ArrayList<Job> activeJobs = jobManager.getActiveJobs();

        if (activeJobs.isEmpty()) {
            System.out.println("No active job postings found at this moment.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        for (Job job : activeJobs) {
            job.displaySummary();
        }
        ConsoleUI.printDivider();

        System.out.print("Enter Job ID to view full details (or press ENTER to return): ");
        String jobId = scanner.nextLine().trim();
        if (!jobId.isEmpty()) {
            Job job = jobManager.getJobById(jobId);
            if (job != null && job.isActive()) {
                job.displayDetails();

                // Live Compatibility Preview for candidate
                double previewScore = matchEngine.calculateMatchScore(candidate, job);
                boolean eligibleCgpa = matchEngine.checkCgpaEligibility(candidate, job);
                ArrayList<String> matched = matchEngine.getMatchedSkills(candidate, job);
                ArrayList<String> missing = matchEngine.getMissingSkills(candidate, job);

                System.out.println("\n--- YOUR COMPATIBILITY PREVIEW ---");
                System.out.printf("Skill Match Percentage: %.1f%%%n", previewScore);
                System.out.println("Matched Skills        : " + (matched.isEmpty() ? "None" : String.join(", ", matched)));
                System.out.println("Missing Skills        : " + (missing.isEmpty() ? "None" : String.join(", ", missing)));
                System.out.printf("CGPA Eligibility      : %s (Your CGPA: %.2f | Required: %.2f)%n",
                        (eligibleCgpa ? "ELIGIBLE" : "INELIGIBLE - Below Minimum Cutoff"), candidate.getCgpa(), job.getMinCgpa());
                System.out.println("----------------------------------");

                boolean hasApplied = applicationManager.hasCandidateApplied(candidate.getUserId(), job.getJobId());
                if (hasApplied) {
                    System.out.println("[NOTE] You have already applied for this job.");
                } else {
                    System.out.print("\nDo you want to apply for this job now? (Y/N): ");
                    String ans = scanner.nextLine().trim();
                    if (ans.equalsIgnoreCase("Y")) {
                        try {
                            Application app = applicationManager.applyForJob(candidate.getUserId(), job.getJobId());
                            matchEngine.evaluateCandidate(candidate, job, app);
                            saveApplicationState();
                            ConsoleUI.printSuccess("Application submitted successfully! Application ID: " + app.getApplicationId());
                        } catch (DuplicateApplicationException e) {
                            ConsoleUI.printError(e.getMessage());
                        } catch (InvalidInputException e) {
                            ConsoleUI.printError(e.getMessage());
                        }
                    }
                }
            } else {
                ConsoleUI.printError("Job not found or is currently closed.");
            }
        }
        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleCandidateApply(Candidate candidate) {
        ConsoleUI.printHeader("Apply for a Job Opening");
        ArrayList<Job> activeJobs = jobManager.getActiveJobs();
        if (activeJobs.isEmpty()) {
            System.out.println("No active job postings are available.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        for (Job job : activeJobs) {
            boolean already = applicationManager.hasCandidateApplied(candidate.getUserId(), job.getJobId());
            System.out.printf("[%s] %-25s | Min CGPA: %-4.2f | Status: %s%n",
                    job.getJobId(), job.getTitle(), job.getMinCgpa(), (already ? "[ALREADY APPLIED]" : "[OPEN]"));
        }
        ConsoleUI.printDivider();

        String jobId = ConsoleUI.readString(scanner, "Enter Job ID to submit application (or CANCEL): ", true);
        if (jobId.equalsIgnoreCase("CANCEL")) return;

        Job targetJob = jobManager.getJobById(jobId);
        if (targetJob == null || !targetJob.isActive()) {
            ConsoleUI.printError("Invalid Job ID or job is not active.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        try {
            Application app = applicationManager.applyForJob(candidate.getUserId(), targetJob.getJobId());
            matchEngine.evaluateCandidate(candidate, targetJob, app);
            saveApplicationState();
            ConsoleUI.printSuccess("Application submitted successfully! Application ID: " + app.getApplicationId());
        } catch (DuplicateApplicationException e) {
            ConsoleUI.printError("Duplicate Application: " + e.getMessage());
        } catch (InvalidInputException e) {
            ConsoleUI.printError("Error: " + e.getMessage());
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleTrackApplications(Candidate candidate) {
        ConsoleUI.printHeader("Your Applications & Interview Schedule");
        ArrayList<Application> apps = applicationManager.getApplicationsByCandidate(candidate.getUserId());

        if (apps.isEmpty()) {
            System.out.println("You have not applied to any jobs yet.");
        } else {
            System.out.println("SUBMITTED APPLICATIONS:");
            System.out.printf("%-10s | %-10s | %-25s | %-8s | %-20s | %s%n",
                    "App ID", "Job ID", "Job Title", "Match %", "Status", "Remarks");
            ConsoleUI.printDivider();

            for (Application app : apps) {
                Job job = jobManager.getJobById(app.getJobId());
                String jobTitle = (job != null) ? job.getTitle() : "Unknown Job";
                if (jobTitle.length() > 25) {
                    jobTitle = jobTitle.substring(0, 22) + "...";
                }
                System.out.printf("%-10s | %-10s | %-25s | %6.1f%% | %-20s | %s%n",
                        app.getApplicationId(),
                        app.getJobId(),
                        jobTitle,
                        app.getMatchScore(),
                        app.getStatus().name(),
                        app.getRemarks());
            }
        }

        // Show scheduled interviews for this candidate
        ArrayList<Interview> interviews = interviewManager.getInterviewsByCandidate(candidate.getUserId());
        if (!interviews.isEmpty()) {
            System.out.println();
            ConsoleUI.printDivider();
            System.out.println("SCHEDULED INTERVIEWS:");
            for (Interview interview : interviews) {
                interview.displaySummary();
            }
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    // =========================================================================
    // RECRUITER WORKFLOW MENU
    // =========================================================================

    private static void showRecruiterMenu(Recruiter recruiter) {
        ConsoleUI.printHeader("Recruiter Portal | " + recruiter.getName() + " [" + recruiter.getCompanyName() + "]");
        System.out.println(" 1. Create Job");
        System.out.println(" 2. Manage Jobs");
        System.out.println(" 3. View Applications");
        System.out.println(" 4. Evaluate Candidates (Matching Engine)");
        System.out.println(" 5. View Candidate Rankings");
        System.out.println(" 6. View Shortlisted Candidates");
        System.out.println(" 7. Interview Management");
        System.out.println(" 8. Generate Reports");
        System.out.println(" 9. Logout");
        ConsoleUI.printDivider();

        int choice = ConsoleUI.readInt(scanner, "Enter your choice [1-9]: ", 1, 9);
        switch (choice) {
            case 1:
                handleCreateJob(recruiter);
                break;
            case 2:
                handleManageJobs(recruiter);
                break;
            case 3:
                handleViewApplications(recruiter);
                break;
            case 4:
                handleEvaluateCandidates(recruiter);
                break;
            case 5:
                handleViewRankings(recruiter);
                break;
            case 6:
                handleViewShortlisted(recruiter);
                break;
            case 7:
                handleInterviewManagementMenu(recruiter);
                break;
            case 8:
                handleGenerateReports(recruiter);
                break;
            case 9:
                currentUser = null;
                ConsoleUI.printInfo("Logged out successfully.");
                break;
        }
    }

    private static void handleCreateJob(Recruiter recruiter) {
        ConsoleUI.printHeader("Create New Job Vacancy");
        String title = ConsoleUI.readString(scanner, "Enter Job Title: ", true);
        String company = recruiter.getCompanyName();
        System.out.println("Company: " + company);
        String desc = ConsoleUI.readString(scanner, "Enter Job Description: ", true);

        System.out.print("Enter Required Skills (comma-separated, e.g. Java, Spring Boot, SQL, Git): ");
        String skillsLine = scanner.nextLine();
        ArrayList<String> skills = parseCommaSeparated(skillsLine);
        if (skills.isEmpty()) {
            ConsoleUI.printError("At least one required skill must be specified.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        double minCgpa = ConsoleUI.readDouble(scanner, "Enter Minimum CGPA Requirement [0.0 - 10.0]: ", 0.0, 10.0);
        int minExp = ConsoleUI.readInt(scanner, "Enter Minimum Experience in Years [0 - 50]: ", 0, 50);
        String location = ConsoleUI.readString(scanner, "Enter Work Location (e.g. Bangalore / Remote): ", false);

        try {
            Job job = jobManager.createJob(recruiter.getUserId(), title, company, desc, skills, minCgpa, minExp, location);
            saveApplicationState();
            ConsoleUI.printSuccess("Job created successfully! Job ID: " + job.getJobId());
        } catch (InvalidInputException e) {
            ConsoleUI.printError("Failed to create job: " + e.getMessage());
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleManageJobs(Recruiter recruiter) {
        ConsoleUI.printHeader("Manage Your Posted Jobs");
        ArrayList<Job> myJobs = jobManager.getJobsByRecruiter(recruiter.getUserId());

        if (myJobs.isEmpty()) {
            System.out.println("You have not created any job vacancies yet.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        for (Job job : myJobs) {
            job.displaySummary();
        }
        ConsoleUI.printDivider();

        System.out.print("Enter Job ID to modify (or press ENTER to return): ");
        String jobId = scanner.nextLine().trim();
        if (jobId.isEmpty()) return;

        Job job = jobManager.getJobById(jobId);
        if (job == null || !job.getRecruiterId().equalsIgnoreCase(recruiter.getUserId())) {
            ConsoleUI.printError("Job not found or unauthorized access.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        job.displayDetails();
        System.out.println("Actions: [1] Edit Details | [2] Toggle Active/Closed Status | [3] Return");
        int action = ConsoleUI.readInt(scanner, "Select action [1-3]: ", 1, 3);

        if (action == 1) {
            String newTitle = ConsoleUI.readString(scanner, "Enter New Title (leave blank to keep current): ", false);
            if (newTitle.isEmpty()) newTitle = job.getTitle();

            String newDesc = ConsoleUI.readString(scanner, "Enter New Description (leave blank to keep current): ", false);
            if (newDesc.isEmpty()) newDesc = job.getDescription();

            System.out.print("Enter Required Skills (comma-separated, leave blank to keep current): ");
            String skillsLine = scanner.nextLine();
            ArrayList<String> newSkills = skillsLine.trim().isEmpty() ? job.getRequiredSkills() : parseCommaSeparated(skillsLine);

            double newMinCgpa = ConsoleUI.readDouble(scanner, "Enter Minimum CGPA [0.0 - 10.0]: ", 0.0, 10.0);
            int newMinExp = ConsoleUI.readInt(scanner, "Enter Minimum Experience [0 - 50]: ", 0, 50);
            String newLoc = ConsoleUI.readString(scanner, "Enter Location: ", false);

            try {
                jobManager.updateJob(job.getJobId(), newTitle, newDesc, newSkills, newMinCgpa, newMinExp, newLoc, job.isActive());
                saveApplicationState();
                ConsoleUI.printSuccess("Job details updated successfully.");
            } catch (InvalidInputException e) {
                ConsoleUI.printError(e.getMessage());
            }
        } else if (action == 2) {
            boolean nextState = !job.isActive();
            job.setActive(nextState);
            saveApplicationState();
            ConsoleUI.printSuccess("Job status updated to: " + (nextState ? "ACTIVE" : "CLOSED"));
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleViewApplications(Recruiter recruiter) {
        ConsoleUI.printHeader("View Job Applications");
        ArrayList<Job> myJobs = jobManager.getJobsByRecruiter(recruiter.getUserId());

        if (myJobs.isEmpty()) {
            System.out.println("You have no posted jobs.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        for (Job job : myJobs) {
            ArrayList<Application> apps = applicationManager.getApplicationsByJob(job.getJobId());
            System.out.printf("[%s] %-25s | Applications: %d%n", job.getJobId(), job.getTitle(), apps.size());
        }
        ConsoleUI.printDivider();

        System.out.print("Enter Job ID to view applications (or press ENTER to return): ");
        String jobId = scanner.nextLine().trim();
        if (jobId.isEmpty()) return;

        Job targetJob = jobManager.getJobById(jobId);
        if (targetJob == null) {
            ConsoleUI.printError("Job not found.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        ArrayList<Application> apps = applicationManager.getApplicationsByJob(targetJob.getJobId());
        if (apps.isEmpty()) {
            System.out.println("No applications received for this job yet.");
        } else {
            System.out.printf("%-10s | %-10s | %-20s | %-7s | %-8s | %-16s | %s%n",
                    "App ID", "Cand ID", "Candidate Name", "CGPA", "Match %", "Status", "Remarks");
            ConsoleUI.printDivider();

            for (Application app : apps) {
                Candidate cand = (Candidate) authManager.getUserById(app.getCandidateId());
                String candName = (cand != null) ? cand.getName() : "Unknown";
                double candCgpa = (cand != null) ? cand.getCgpa() : 0.0;
                if (candName.length() > 20) {
                    candName = candName.substring(0, 17) + "...";
                }

                System.out.printf("%-10s | %-10s | %-20s | %6.2f | %6.1f%% | %-16s | %s%n",
                        app.getApplicationId(),
                        app.getCandidateId(),
                        candName,
                        candCgpa,
                        app.getMatchScore(),
                        app.getStatus().name(),
                        app.getRemarks());
            }
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    // =========================================================================
    // MODULE 3: MATCHING ENGINE, RANKINGS, & SHORTLISTING (SHANAYA'S FOCUS)
    // =========================================================================

    private static void handleEvaluateCandidates(Recruiter recruiter) {
        ConsoleUI.printHeader("Module 3 - Matching Engine Evaluation");
        Job job = selectJobForRecruiter(recruiter);
        if (job == null) return;

        ArrayList<Application> apps = applicationManager.getApplicationsByJob(job.getJobId());
        if (apps.isEmpty()) {
            System.out.println("No candidate applications have been submitted for " + job.getJobId() + " yet.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        System.out.println("Target Job: [" + job.getJobId() + "] " + job.getTitle());
        System.out.println("Mandatory Min CGPA Requirement : " + job.getMinCgpa());
        System.out.println("Required Skills                : " + String.join(", ", job.getRequiredSkills()));
        ConsoleUI.printDivider();

        // Run evaluation through MatchEngine
        ArrayList<CandidateMatchResult> results = matchEngine.evaluateJobApplications(job, apps, authManager);

        System.out.println("EVALUATION RESULTS (Formula: Matched Skills / Total Required Skills * 100):");
        System.out.printf("%-10s | %-18s | %-6s | %-8s | %-11s | %-15s | %-15s%n",
                "Cand ID", "Name", "CGPA", "Match %", "CGPA Cutoff", "Matched Skills", "Missing Skills");
        ConsoleUI.printDivider();

        for (CandidateMatchResult res : results) {
            String name = res.getCandidate().getName();
            if (name.length() > 18) name = name.substring(0, 15) + "...";

            String matchedStr = String.join(",", res.getMatchedSkills());
            if (matchedStr.length() > 15) matchedStr = matchedStr.substring(0, 12) + "...";

            String missingStr = String.join(",", res.getMissingSkills());
            if (missingStr.length() > 15) missingStr = missingStr.substring(0, 12) + "...";

            System.out.printf("%-10s | %-18s | %6.2f | %6.1f%% | %-11s | %-15s | %-15s%n",
                    res.getCandidate().getUserId(),
                    name,
                    res.getCandidate().getCgpa(),
                    res.getMatchPercentage(),
                    (res.isCgpaEligible() ? "PASS (>= " + job.getMinCgpa() + ")" : "FAIL (< " + job.getMinCgpa() + ")"),
                    matchedStr,
                    missingStr);
        }

        saveApplicationState();
        ConsoleUI.printDivider();

        System.out.print("Do you want to auto-shortlist eligible candidates meeting a score threshold? (Y/N): ");
        String ans = scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("Y")) {
            double threshold = ConsoleUI.readDouble(scanner, "Enter minimum matching score threshold % [0.0 - 100.0]: ", 0.0, 100.0);
            ArrayList<CandidateMatchResult> ranked = matchEngine.rankCandidates(results);
            ArrayList<CandidateMatchResult> shortlisted = matchEngine.shortlistCandidates(job, ranked, threshold, applicationManager);
            saveApplicationState();

            ConsoleUI.printSuccess("Auto-shortlisting complete!");
            System.out.println("Successfully shortlisted " + shortlisted.size() + " eligible candidate(s).");
            System.out.println("Note: Candidates failing minimum CGPA (" + job.getMinCgpa() + ") were automatically disqualified.");
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleViewRankings(Recruiter recruiter) {
        ConsoleUI.printHeader("Candidate Rankings (Highest to Lowest)");
        Job job = selectJobForRecruiter(recruiter);
        if (job == null) return;

        ArrayList<Application> apps = applicationManager.getApplicationsByJob(job.getJobId());
        if (apps.isEmpty()) {
            System.out.println("No applications to rank for " + job.getJobId() + ".");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        ArrayList<CandidateMatchResult> results = matchEngine.evaluateJobApplications(job, apps, authManager);
        ArrayList<CandidateMatchResult> rankedResults = matchEngine.rankCandidates(results);

        System.out.println("Job: [" + job.getJobId() + "] " + job.getTitle() + " (Min CGPA Cutoff: " + job.getMinCgpa() + ")");
        ConsoleUI.printDivider();

        int rank = 1;
        System.out.println("ELIGIBLE CANDIDATE RANKINGS (Ranked by Match % DESC, then CGPA DESC):");
        System.out.printf("%-6s | %-10s | %-20s | %-8s | %-6s | %-14s | %-10s%n",
                "Rank", "Cand ID", "Candidate Name", "Match %", "CGPA", "Weighted Score", "Status");
        ConsoleUI.printDivider();

        ArrayList<CandidateMatchResult> disqualified = new ArrayList<CandidateMatchResult>();

        for (CandidateMatchResult res : rankedResults) {
            if (res.isCgpaEligible()) {
                String name = res.getCandidate().getName();
                if (name.length() > 20) name = name.substring(0, 17) + "...";

                System.out.printf("#%-5d | %-10s | %-20s | %6.1f%% | %6.2f | %6.2f / 100   | %-10s%n",
                        rank++,
                        res.getCandidate().getUserId(),
                        name,
                        res.getMatchPercentage(),
                        res.getCandidate().getCgpa(),
                        res.getWeightedScore(),
                        res.getApplication().getStatus().name());
            } else {
                disqualified.add(res);
            }
        }

        if (rank == 1) {
            System.out.println("  No candidates met the mandatory CGPA eligibility cutoff.");
        }

        if (!disqualified.isEmpty()) {
            System.out.println();
            ConsoleUI.printDivider();
            System.out.println("DISQUALIFIED CANDIDATES (Failed Mandatory CGPA Cutoff - Excluded from Shortlisting):");
            System.out.printf("%-10s | %-20s | %-8s | %-6s | %s%n",
                    "Cand ID", "Candidate Name", "Match %", "CGPA", "Reason for Disqualification");
            ConsoleUI.printDivider();

            for (CandidateMatchResult res : disqualified) {
                String name = res.getCandidate().getName();
                if (name.length() > 20) name = name.substring(0, 17) + "...";

                System.out.printf("%-10s | %-20s | %6.1f%% | %6.2f | CGPA below minimum required (%.2f)%n",
                        res.getCandidate().getUserId(),
                        name,
                        res.getMatchPercentage(),
                        res.getCandidate().getCgpa(),
                        job.getMinCgpa());
            }
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleViewShortlisted(Recruiter recruiter) {
        ConsoleUI.printHeader("View Shortlisted Candidates");
        Job job = selectJobForRecruiter(recruiter);
        if (job == null) return;

        ArrayList<Application> apps = applicationManager.getApplicationsByJob(job.getJobId());
        ArrayList<Application> shortlisted = new ArrayList<Application>();
        for (Application app : apps) {
            if (app.getStatus() == ApplicationStatus.SHORTLISTED ||
                app.getStatus() == ApplicationStatus.INTERVIEW_SCHEDULED ||
                app.getStatus() == ApplicationStatus.SELECTED) {
                shortlisted.add(app);
            }
        }

        if (shortlisted.isEmpty()) {
            System.out.println("No candidates currently shortlisted for Job " + job.getJobId() + ".");
            System.out.println("Tip: Use 'Evaluate Candidates' menu to run the Match Engine and shortlist top applicants.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        System.out.printf("%-10s | %-10s | %-20s | %-8s | %-6s | %-20s%n",
                "App ID", "Cand ID", "Candidate Name", "Match %", "CGPA", "Current Status");
        ConsoleUI.printDivider();

        for (Application app : shortlisted) {
            Candidate cand = (Candidate) authManager.getUserById(app.getCandidateId());
            String name = (cand != null) ? cand.getName() : "Unknown";
            double cgpa = (cand != null) ? cand.getCgpa() : 0.0;
            if (name.length() > 20) name = name.substring(0, 17) + "...";

            System.out.printf("%-10s | %-10s | %-20s | %6.1f%% | %6.2f | %-20s%n",
                    app.getApplicationId(),
                    app.getCandidateId(),
                    name,
                    app.getMatchScore(),
                    cgpa,
                    app.getStatus().name());
        }

        ConsoleUI.printDivider();
        System.out.print("Do you want to schedule an interview for any of these candidates? (Y/N): ");
        String ans = scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("Y")) {
            String appId = ConsoleUI.readString(scanner, "Enter Application ID: ", true);
            Application targetApp = applicationManager.getApplicationById(appId);
            if (targetApp != null && shortlisted.contains(targetApp)) {
                scheduleInterviewForApp(recruiter, targetApp);
            } else {
                ConsoleUI.printError("Invalid Application ID or candidate not in shortlisted set.");
            }
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    // =========================================================================
    // INTERVIEW MANAGEMENT SUB-WORKFLOW
    // =========================================================================

    private static void handleInterviewManagementMenu(Recruiter recruiter) {
        boolean inMenu = true;
        while (inMenu) {
            ConsoleUI.printHeader("Interview Management Portal");
            System.out.println(" 1. Schedule Interview for Candidate");
            System.out.println(" 2. View Scheduled Interviews");
            System.out.println(" 3. Update Interview Status & Feedback");
            System.out.println(" 4. Update Final Application Decision (Offer/Reject)");
            System.out.println(" 5. Back to Recruiter Menu");
            ConsoleUI.printDivider();

            int choice = ConsoleUI.readInt(scanner, "Enter choice [1-5]: ", 1, 5);
            switch (choice) {
                case 1:
                    handleScheduleInterview(recruiter);
                    break;
                case 2:
                    handleViewInterviews(recruiter);
                    break;
                case 3:
                    handleUpdateInterviewStatus(recruiter);
                    break;
                case 4:
                    handleFinalDecision(recruiter);
                    break;
                case 5:
                    inMenu = false;
                    break;
            }
        }
    }

    private static void handleScheduleInterview(Recruiter recruiter) {
        ConsoleUI.printHeader("Schedule Interview Round");
        String appId = ConsoleUI.readString(scanner, "Enter Target Application ID (e.g. APP-101): ", true);
        Application app = applicationManager.getApplicationById(appId);

        if (app == null) {
            ConsoleUI.printError("Application not found.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        Job job = jobManager.getJobById(app.getJobId());
        if (job == null || !job.getRecruiterId().equalsIgnoreCase(recruiter.getUserId())) {
            ConsoleUI.printError("Access denied: You are not the recruiter managing this job.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        scheduleInterviewForApp(recruiter, app);
        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void scheduleInterviewForApp(Recruiter recruiter, Application app) {
        Candidate candidate = (Candidate) authManager.getUserById(app.getCandidateId());
        String candName = (candidate != null) ? candidate.getName() : app.getCandidateId();

        System.out.println("\nScheduling Interview for: " + candName + " (App: " + app.getApplicationId() + ")");
        System.out.println("Select Interview Round:");
        InterviewRound[] rounds = InterviewRound.values();
        for (int i = 0; i < rounds.length; i++) {
            System.out.println(" " + (i + 1) + ". " + rounds[i].getDescription());
        }

        int roundIdx = ConsoleUI.readInt(scanner, "Choose round [1-" + rounds.length + "]: ", 1, rounds.length) - 1;
        InterviewRound selectedRound = rounds[roundIdx];

        String dateTime = ConsoleUI.readString(scanner, "Enter Date and Time (e.g. 2026-11-25 15:00): ", true);
        String mode = ConsoleUI.readString(scanner, "Enter Mode / Location (e.g. Google Meet or Room 402): ", false);
        if (mode.isEmpty()) mode = "Online - Google Meet";

        try {
            Interview interview = interviewManager.scheduleInterview(
                    app.getApplicationId(),
                    app.getCandidateId(),
                    app.getJobId(),
                    recruiter.getUserId(),
                    selectedRound,
                    dateTime,
                    mode
            );

            // Update application status to INTERVIEW_SCHEDULED
            applicationManager.updateStatus(app.getApplicationId(), ApplicationStatus.INTERVIEW_SCHEDULED,
                    "Interview scheduled: " + selectedRound.name() + " on " + dateTime);

            saveApplicationState();
            ConsoleUI.printSuccess("Interview successfully scheduled! Interview ID: " + interview.getInterviewId());
        } catch (InvalidInputException e) {
            ConsoleUI.printError("Failed to schedule interview: " + e.getMessage());
        }
    }

    private static void handleViewInterviews(Recruiter recruiter) {
        ConsoleUI.printHeader("All Scheduled Interviews");
        ArrayList<Interview> interviews = interviewManager.getInterviewsByRecruiter(recruiter.getUserId());

        if (interviews.isEmpty()) {
            System.out.println("No interviews currently scheduled by your account.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        for (Interview i : interviews) {
            i.displaySummary();
        }
        ConsoleUI.printDivider();

        System.out.print("Enter Interview ID to view full details (or press ENTER to return): ");
        String intId = scanner.nextLine().trim();
        if (!intId.isEmpty()) {
            Interview interview = interviewManager.getInterviewById(intId);
            if (interview != null) {
                interview.displayDetails();
            } else {
                ConsoleUI.printError("Interview ID not found.");
            }
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleUpdateInterviewStatus(Recruiter recruiter) {
        ConsoleUI.printHeader("Update Interview Outcome & Feedback");
        String intId = ConsoleUI.readString(scanner, "Enter Interview ID to update: ", true);
        Interview interview = interviewManager.getInterviewById(intId);

        if (interview == null || !interview.getRecruiterId().equalsIgnoreCase(recruiter.getUserId())) {
            ConsoleUI.printError("Interview not found or unauthorized.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        interview.displayDetails();
        System.out.println("Select New Interview Status:");
        InterviewStatus[] statuses = InterviewStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println(" " + (i + 1) + ". " + statuses[i].getDescription());
        }

        int statusIdx = ConsoleUI.readInt(scanner, "Choose status [1-" + statuses.length + "]: ", 1, statuses.length) - 1;
        InterviewStatus newStatus = statuses[statusIdx];
        String feedback = ConsoleUI.readString(scanner, "Enter Interviewer Feedback / Notes: ", true);

        interviewManager.updateInterviewStatus(interview.getInterviewId(), newStatus, feedback);
        saveApplicationState();
        ConsoleUI.printSuccess("Interview status updated to: " + newStatus.name());
        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleFinalDecision(Recruiter recruiter) {
        ConsoleUI.printHeader("Final Application Decision (Offer / Reject)");
        String appId = ConsoleUI.readString(scanner, "Enter Application ID: ", true);
        Application app = applicationManager.getApplicationById(appId);

        if (app == null) {
            ConsoleUI.printError("Application not found.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        Job job = jobManager.getJobById(app.getJobId());
        if (job == null || !job.getRecruiterId().equalsIgnoreCase(recruiter.getUserId())) {
            ConsoleUI.printError("Unauthorized access to this application.");
            ConsoleUI.pressEnterToContinue(scanner);
            return;
        }

        System.out.println("Current Status: " + app.getStatus().getDescription());
        System.out.println(" 1. Extend Offer (Mark SELECTED)");
        System.out.println(" 2. Reject Candidate (Mark REJECTED)");
        System.out.println(" 3. Cancel");

        int dec = ConsoleUI.readInt(scanner, "Choose decision [1-3]: ", 1, 3);
        if (dec == 1) {
            String remarks = ConsoleUI.readString(scanner, "Enter Offer Remarks: ", false);
            applicationManager.updateStatus(app.getApplicationId(), ApplicationStatus.SELECTED,
                    remarks.isEmpty() ? "Offer extended and candidate selected." : remarks);
            saveApplicationState();
            ConsoleUI.printSuccess("Application status successfully updated to SELECTED!");
        } else if (dec == 2) {
            String remarks = ConsoleUI.readString(scanner, "Enter Rejection Reason: ", false);
            applicationManager.updateStatus(app.getApplicationId(), ApplicationStatus.REJECTED,
                    remarks.isEmpty() ? "Candidate not selected after final review." : remarks);
            saveApplicationState();
            ConsoleUI.printSuccess("Application status updated to REJECTED.");
        }

        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleGenerateReports(Recruiter recruiter) {
        ConsoleUI.printHeader("Recruitment Reports & Analytics");
        System.out.println(" 1. View System Executive Summary");
        System.out.println(" 2. View Job-Wise Applications Report");
        System.out.println(" 3. View My Recruiter Performance Report");
        System.out.println(" 4. Back");

        int choice = ConsoleUI.readInt(scanner, "Select report [1-4]: ", 1, 4);
        switch (choice) {
            case 1:
                reportManager.printExecutiveSummary();
                break;
            case 2:
                reportManager.printJobWiseReport();
                break;
            case 3:
                reportManager.printRecruiterReport(recruiter.getUserId());
                break;
            case 4:
                return;
        }
        ConsoleUI.pressEnterToContinue(scanner);
    }

    // =========================================================================
    // ADMIN WORKFLOW MENU
    // =========================================================================

    private static void showAdminMenu(Admin admin) {
        ConsoleUI.printHeader("Administrator Control Panel | " + admin.getName());
        System.out.println(" 1. View All Registered Users");
        System.out.println(" 2. View Recruiters & Companies");
        System.out.println(" 3. View System Recruitment Statistics");
        System.out.println(" 4. Logout");
        ConsoleUI.printDivider();

        int choice = ConsoleUI.readInt(scanner, "Enter choice [1-4]: ", 1, 4);
        switch (choice) {
            case 1:
                handleAdminViewUsers();
                break;
            case 2:
                handleAdminViewRecruiters();
                break;
            case 3:
                reportManager.printExecutiveSummary();
                reportManager.printJobWiseReport();
                ConsoleUI.pressEnterToContinue(scanner);
                break;
            case 4:
                currentUser = null;
                ConsoleUI.printInfo("Admin logged out successfully.");
                break;
        }
    }

    private static void handleAdminViewUsers() {
        ConsoleUI.printHeader("System Registered Users");
        ArrayList<User> users = authManager.getAllUsers();
        System.out.printf("%-10s | %-12s | %-22s | %-26s%n", "User ID", "Role", "Name", "Email");
        ConsoleUI.printDivider();
        for (User u : users) {
            String name = u.getName();
            if (name.length() > 22) name = name.substring(0, 19) + "...";
            System.out.printf("%-10s | %-12s | %-22s | %-26s%n",
                    u.getUserId(), u.getRole(), name, u.getEmail());
        }
        ConsoleUI.pressEnterToContinue(scanner);
    }

    private static void handleAdminViewRecruiters() {
        ConsoleUI.printHeader("Registered Corporate Recruiters");
        ArrayList<Recruiter> recruiters = authManager.getRecruiters();
        System.out.printf("%-10s | %-20s | %-24s | %-18s | %s%n",
                "Recruiter", "Name", "Company", "Department", "Email");
        ConsoleUI.printDivider();
        for (Recruiter r : recruiters) {
            String name = r.getName();
            if (name.length() > 20) name = name.substring(0, 17) + "...";
            String comp = r.getCompanyName();
            if (comp.length() > 24) comp = comp.substring(0, 21) + "...";
            String dept = r.getDepartment();
            if (dept.length() > 18) dept = dept.substring(0, 15) + "...";

            System.out.printf("%-10s | %-20s | %-24s | %-18s | %s%n",
                    r.getUserId(), name, comp, dept, r.getEmail());
        }
        ConsoleUI.pressEnterToContinue(scanner);
    }

    // =========================================================================
    // UTILITY HELPERS
    // =========================================================================

    private static Job selectJobForRecruiter(Recruiter recruiter) {
        ArrayList<Job> myJobs = jobManager.getJobsByRecruiter(recruiter.getUserId());
        if (myJobs.isEmpty()) {
            System.out.println("You have no posted jobs.");
            ConsoleUI.pressEnterToContinue(scanner);
            return null;
        }

        System.out.println("Your Posted Jobs:");
        for (Job j : myJobs) {
            System.out.printf("[%s] %s (%s)%n", j.getJobId(), j.getTitle(), (j.isActive() ? "Active" : "Closed"));
        }
        ConsoleUI.printDivider();

        String jobId = ConsoleUI.readString(scanner, "Enter Job ID: ", true);
        Job job = jobManager.getJobById(jobId);
        if (job == null || !job.getRecruiterId().equalsIgnoreCase(recruiter.getUserId())) {
            ConsoleUI.printError("Invalid Job ID or unauthorized access.");
            ConsoleUI.pressEnterToContinue(scanner);
            return null;
        }
        return job;
    }

    // Splits a comma-separated string into a clean ArrayList of strings
    private static ArrayList<String> parseCommaSeparated(String input) {
        ArrayList<String> list = new ArrayList<String>();
        if (input != null && !input.trim().isEmpty()) {
            String[] tokens = input.split(",");
            for (String t : tokens) {
                String trimmed = t.trim();
                if (!trimmed.isEmpty()) {
                    list.add(trimmed);
                }
            }
        }
        return list;
    }
}

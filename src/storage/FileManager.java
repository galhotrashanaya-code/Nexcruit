package storage;

import applications.Application;
import applications.ApplicationManager;
import auth.Admin;
import auth.AuthManager;
import auth.Candidate;
import auth.Recruiter;
import interviews.Interview;
import interviews.InterviewManager;
import jobs.Job;
import jobs.JobManager;
import utils.ConsoleUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Handles File Handling and Object Serialization using standard Java I/O streams.
 * Concepts used:
 * - File Handling (File class, mkdirs())
 * - Object Serialization (ObjectOutputStream and ObjectInputStream)
 * - Exception Handling (IOException, ClassNotFoundException)
 */
public class FileManager {

    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "data" + File.separator + "nexcruit_data.dat";

    public FileManager() {
        // Ensure data directory exists
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Saves the entire application state into a binary file
    public boolean saveData(AuthManager authManager, JobManager jobManager,
                            ApplicationManager applicationManager, InterviewManager interviewManager) {
        DataStore store = new DataStore(
                authManager.getAllUsers(),
                jobManager.getAllJobs(),
                applicationManager.getAllApplications(),
                interviewManager.getAllInterviews()
        );

        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(store);
            out.close();
            fileOut.close();
            return true;
        } catch (IOException e) {
            ConsoleUI.printError("Could not save data to file: " + e.getMessage());
            return false;
        }
    }

    // Loads previously saved application data or seeds default demo data if no file exists
    public DataStore loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists() || file.length() == 0) {
            ConsoleUI.printInfo("Initializing starter accounts and sample job data...");
            DataStore starter = createInitialSeedData();
            // Automatically save starter data so file is created
            try {
                FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(starter);
                out.close();
                fileOut.close();
            } catch (IOException ignored) {
            }
            return starter;
        }

        try {
            FileInputStream fileIn = new FileInputStream(DATA_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object obj = in.readObject();
            in.close();
            fileIn.close();

            if (obj instanceof DataStore) {
                return (DataStore) obj;
            }
        } catch (IOException e) {
            ConsoleUI.printInfo("Previous file could not be read. Loading fresh starter data.");
        } catch (ClassNotFoundException e) {
            ConsoleUI.printInfo("Class definition mismatch. Loading fresh starter data.");
        }

        return createInitialSeedData();
    }

    // Seeds realistic starter data for college project demonstration
    public DataStore createInitialSeedData() {
        DataStore store = new DataStore();

        // 1. Admin account
        Admin admin = new Admin("ADM-101", "System Administrator", "admin@nexcruit.com", "admin123");
        store.getUsers().add(admin);

        // 2. Recruiter account
        Recruiter recruiter = new Recruiter("REC-101", "Sarah Jenkins", "sarah@techcorp.com", "recruiter123",
                "TechCorp Solutions", "Engineering Talent Acquisition", "+1-555-0199");
        store.getUsers().add(recruiter);

        // 3. Candidates with varied skills and CGPA
        ArrayList<String> alexSkills = new ArrayList<String>();
        alexSkills.add("Java");
        alexSkills.add("Spring Boot");
        alexSkills.add("SQL");
        alexSkills.add("Git");
        alexSkills.add("Docker");
        Candidate alex = new Candidate("CAN-101", "Alex Rivera", "alex@gmail.com", "alex123",
                "+1-555-0101", "B.Tech Computer Science", 8.4, 1, alexSkills);

        ArrayList<String> priyaSkills = new ArrayList<String>();
        priyaSkills.add("Java");
        priyaSkills.add("Python");
        priyaSkills.add("HTML");
        priyaSkills.add("CSS");
        Candidate priya = new Candidate("CAN-102", "Priya Sharma", "priya@gmail.com", "priya123",
                "+1-555-0102", "BCA Information Technology", 6.8, 0, priyaSkills);

        ArrayList<String> devonSkills = new ArrayList<String>();
        devonSkills.add("Java");
        devonSkills.add("Spring Boot");
        devonSkills.add("SQL");
        devonSkills.add("Microservices");
        Candidate devon = new Candidate("CAN-103", "Devon Miller", "devon@gmail.com", "devon123",
                "+1-555-0103", "M.Tech Software Engineering", 9.1, 2, devonSkills);

        store.getUsers().add(alex);
        store.getUsers().add(priya);
        store.getUsers().add(devon);

        // 4. Sample Job with Minimum CGPA 7.50
        ArrayList<String> jobSkills = new ArrayList<String>();
        jobSkills.add("Java");
        jobSkills.add("Spring Boot");
        jobSkills.add("SQL");
        jobSkills.add("Git");
        Job job1 = new Job("JOB-101", "REC-101", "Junior Java Backend Developer", "TechCorp Solutions",
                "Design and maintain scalable microservices using Java and Spring Boot.",
                jobSkills, 7.5, 0, "Bangalore (Hybrid)");

        ArrayList<String> job2Skills = new ArrayList<String>();
        job2Skills.add("Java");
        job2Skills.add("SQL");
        job2Skills.add("Docker");
        Job job2 = new Job("JOB-102", "REC-101", "Full Stack Cloud Engineer", "TechCorp Solutions",
                "Full-stack cloud application development with modern frameworks.",
                job2Skills, 8.0, 1, "Remote");

        store.getJobs().add(job1);
        store.getJobs().add(job2);

        // 5. Initial Applications demonstrating both eligible and ineligible candidates
        Application app1 = new Application("APP-101", "JOB-101", "CAN-101");
        app1.setMatchScore(100.0);
        app1.setCgpaEligible(true);
        app1.setRemarks("Eligible: 4/4 skills matched, CGPA 8.40 >= 7.50");

        Application app2 = new Application("APP-102", "JOB-101", "CAN-102");
        app2.setMatchScore(25.0);
        app2.setCgpaEligible(false); // Fails 7.50 CGPA requirement!
        app2.setRemarks("Ineligible: Candidate CGPA (6.80) below mandatory requirement (7.50)");

        store.getApplications().add(app1);
        store.getApplications().add(app2);

        return store;
    }
}

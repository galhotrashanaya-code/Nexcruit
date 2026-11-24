# Job & Application Management Module Documentation

**Module Owner**: Himani  
**Component**: Module 2 – Job & Application Management  
**Package**: `package nexcruit;`  
**Target Evaluation**: College Evaluation 1  

---

## 1. Module Overview

The **Job & Application Management Module** provides the core business logic for vacancy lifecycle management and applicant tracking. It allows recruiters to publish, update, and search job openings, and enables candidates to submit applications while enforcing business rules such as preventing duplicate applications for the same opening.

### Key Classes:
1. `Job.java`: Represents a job vacancy entity with encapsulation.
2. `JobManager.java`: Manages job listings, publishing, search by title/skill, and updates using `ArrayList<Job>`.
3. `Application.java`: Represents an application linking actual `Candidate` and `Job` object references.
4. `ApplicationManager.java`: Manages application submissions, duplicate application prevention, and status tracking using `ArrayList<Application>`.

---

## 2. Integration with Team Members

* **With Chesta (Authentication & User Management)**:
  - `Application.java` stores the actual `Candidate` reference from `auth.Candidate`.
  - Methods like `app.getCandidate().getName()`, `app.getCandidate().getCgpa()`, and `app.getCandidate().getSkills()` are used directly.
  - `Job.java` stores `recruiterId` referencing `auth.Recruiter.getUserId()`.

* **With Shanaya (Matching Engine & Interview Management)**:
  - `Job.java` exposes `getRequiredSkills()`, `getMinCgpa()`, and `getMinExperience()` needed by `MatchEngine.calculateMatchScore()` and `checkCgpaEligibility()`.
  - `Application.java` provides `setMatchScore(double)` and `setCgpaEligible(boolean)` so the matching engine can record candidate match percentages.

* **With Sanchi (File Handling & Reports)**:
  - All four classes implement `java.io.Serializable` with `serialVersionUID = 1L`.
  - `ApplicationManager.getAllApplications()` and `JobManager.getAllJobs()` provide data for executive recruitment reporting.

---

## 3. Class and Method Reference for Team Members

### `Job.java`
```java
// Constructors
public Job(String jobId, String title, String companyName, String description,
           ArrayList<String> requiredSkills, double minCgpa, int requiredExperience, int numberOfVacancies)

public Job(String jobId, String recruiterId, String title, String companyName,
           String description, ArrayList<String> requiredSkills, double minCgpa,
           int requiredExperience, int numberOfVacancies, String location)

// Public Methods
public void displayJobDetails()
public void displaySummary()
public String getJobId()
public String getTitle()
public String getCompanyName()
public ArrayList<String> getRequiredSkills()
public double getMinCgpa()
public int getRequiredExperience()
public int getNumberOfVacancies()
public String getStatus()
public boolean isActive()
```

### `JobManager.java`
```java
// Public Methods
public Job createJob(String title, String companyName, String description,
                    ArrayList<String> requiredSkills, double minCgpa,
                    int requiredExperience, int numberOfVacancies) throws InvalidInputException

public void displayAllJobs()
public ArrayList<Job> searchJobsByTitle(String searchTitle)
public ArrayList<Job> searchJobsBySkill(String searchSkill)
public Job getJobById(String jobId)
public ArrayList<Job> getAllJobs()
public ArrayList<Job> getActiveJobs()
public boolean updateJob(String jobId, String title, String description,
                        ArrayList<String> requiredSkills, double minCgpa,
                        int requiredExperience, int numberOfVacancies, String status) throws InvalidInputException
```

### `Application.java`
```java
// Constructors
public Application(String applicationId, Candidate candidate, Job job)
public Application(String applicationId, Candidate candidate, Job job, String applicationDate, String status)

// Public Methods
public void displayApplicationDetails()
public void displaySummary()
public String getApplicationId()
public Candidate getCandidate()
public Job getJob()
public String getCandidateId()
public String getJobId()
public String getStatus()
public void setStatus(String status)
public double getMatchScore()
public void setMatchScore(double matchScore)
public String getRemarks()
public void setRemarks(String remarks)
```

### `ApplicationManager.java`
```java
// Public Methods
public Application applyForJob(Candidate candidate, Job job) throws DuplicateApplicationException, InvalidInputException
public boolean hasCandidateApplied(Candidate candidate, Job job)
public void displayAllApplications()
public void displayApplicationsForJob(String jobId)
public void displayApplicationsByCandidate(String candidateId)
public ArrayList<Application> getApplicationsByJob(String jobId)
public ArrayList<Application> getApplicationsByCandidate(String candidateId)
public Application getApplicationById(String applicationId)
public boolean updateApplicationStatus(String applicationId, String newStatus, String remarks)
public ArrayList<Application> getAllApplications()
```

---

## 4. Test Verification Results

All 11 required evaluation test cases pass in `nexcruit.JobApplicationTest`:

| # | Test Case | Status | Verified Functionality |
|---|---|---|---|
| 1 | Create a new job | **PASS** | Auto-incremented `JOB-101`, vacancies recorded |
| 2 | Display all jobs | **PASS** | Formatted table listing active openings |
| 3 | Search jobs by title | **PASS** | Case-insensitive title search ("backend") |
| 4 | Search jobs by skill | **PASS** | Case-insensitive skill search ("Java") |
| 5 | Apply for a job | **PASS** | Stores actual `Candidate` and `Job` object references |
| 6 | Prevent duplicate applications | **PASS** | Throws `DuplicateApplicationException` on repeated apply |
| 7 | Display candidate applications | **PASS** | Lists applications for candidate `CAN-101` |
| 8 | Display job-wise applications | **PASS** | Lists applications for job `JOB-101` |
| 9 | Update application status | **PASS** | Transitions status to `SHORTLISTED` |
| 10 | Handle invalid inputs | **PASS** | Rejects empty title & zero vacancies |
| 11 | Integration with Candidate & Recruiter | **PASS** | Reads candidate details & recruiter ID successfully |

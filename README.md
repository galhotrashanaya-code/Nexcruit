# NEXCRUIT – Smart Recruitment & Applicant Tracking System

> **A Complete, Modular, Console-Based Java Recruitment Management System built with Core Java and Object-Oriented Programming (OOP) Principles.**

---

## 1. Project Introduction

**NEXCRUIT** is an enterprise-grade recruitment management and applicant tracking system developed in Core Java. Designed for modern corporate hiring, Nexcruit bridges the communication gap between hiring managers and prospective candidates. The platform automates the end-to-end recruitment lifecycle: from vacancy creation and applicant profile ingestion to algorithmic skill matching, academic eligibility filtering, competitive candidate ranking, multi-round interview scheduling, and recruitment analytics reporting.

Nexcruit is specifically engineered to demonstrate master-level application of Java Object-Oriented Programming concepts, clean architecture, and defensive programming for academic and real-world evaluation.

---

## 2. Problem Statement (Evaluation Component 1 – 3 Marks)

### The Real-World Bottleneck
In traditional recruitment pipelines, hiring managers face significant bottlenecks:
1. **Manual Resume Screening Overhead**: Recruiters spend dozens of hours manually parsing hundreds of resumes with varied formats, leading to cognitive fatigue, recruiter bias, and delayed response times.
2. **Eligibility Oversight**: Candidates who fail fundamental non-negotiable thresholds (such as minimum academic CGPA or required qualifications) are frequently forwarded into interview rounds by mistake, consuming valuable interviewer bandwidth.
3. **Lack of Objective Match Metrics**: Without standardized compatibility metrics, evaluations remain subjective and inconsistent across hiring teams.
4. **Duplicate Applications & Pipeline Leakage**: Candidates frequently submit redundant applications for the same opening, skewing recruitment funnel analytics and clogging pipelines.
5. **Disjointed Interview Coordination**: Interview rounds, evaluator feedback, and offer rollouts are often tracked in scattered spreadsheets without unified lifecycle tracking.

### The Nexcruit Solution
Nexcruit addresses these challenges through a unified, automated, terminal-based platform:
* **Algorithmic Skill Matching**: Calculates an exact, transparent percentage score based on required technical skills.
* **Decoupled Eligibility Enforcement**: Strictly isolates mandatory academic cutoffs (CGPA) from skills scoring, automatically barring disqualified candidates from shortlists regardless of high skill match.
* **Integrity & Duplicate Prevention**: Custom exceptions ensure candidates cannot apply multiple times for the same vacancy.
* **Unified Lifecycle Tracking**: Connects job openings, applications, multi-round interviews, and offers in an integrated state.
* **Zero External Dependencies**: Implements complete data persistence via Java Object Serialization without requiring external database setups.

---

## 3. Project Objectives and Scope (Evaluation Component 2 – 2 Marks)

### Primary Objectives
* **Design an Extensible OOP Architecture**: Model a real-world talent acquisition pipeline using robust abstraction, inheritance, encapsulation, and polymorphism.
* **Deliver an Objective Matching Engine**: Implement a transparent, deterministic matching algorithm that evaluates candidate competencies and enforces minimum academic cutoffs.
* **Provide Role-Based Portals**: Deliver dedicated console workflows tailored to Candidates, Recruiters, and Administrators.
* **Ensure Robust Fault Tolerance**: Eliminate application crashes caused by invalid console inputs, improper types, or closed streams.
* **Demonstrate Complete Data Persistence**: Safely serialize and deserialize system state between sessions using native Java File I/O.

### Project Scope
* **Candidate Persona**: Account creation, detailed profile management (CGPA, education, experience, technical skills), job discovery with dynamic compatibility preview, duplicate-safe job application, and pipeline tracking.
* **Recruiter Persona**: Vacancy creation, job management (updates, soft-deletions), candidate evaluation via the Matching Engine, ranked applicant lists, auto-shortlisting, interview round coordination, interviewer feedback logging, and final offer decisions.
* **Administrator Persona**: System-wide user inspection, corporate recruiter tracking, and high-level executive analytics.

---

## 4. Key Features

* **Multi-Role Authentication**: Dedicated portals for Candidates, Recruiters, and Administrators with custom ID generators (`CAN-xxx`, `REC-xxx`, `ADM-xxx`).
* **Live Compatibility Preview**: When browsing openings, candidates instantly view their exact skill match percentage, matched skills, missing skills, and CGPA qualification status.
* **Core Matching Engine (Module 3 Focus)**:
  * Normalized case-insensitive skill comparison.
  * Exact Formula:
    $$\text{Match Percentage} = \left(\frac{\text{Matched Skills}}{\text{Total Required Skills}}\right) \times 100$$
  * Mandatory CGPA Cutoff Enforcement.
  * Disqualification partitioning: candidates failing CGPA are disqualified and barred from shortlisting even if skill match is $100\%$.
  * Extensible weighted multi-criteria scoring preview (Skills 50%, CGPA 25%, Education 15%, Experience 10%).
* **Competitive Ranking**: Multi-tier `Comparator` sorting applicants by eligibility, skill score, CGPA, and alphabetical tiebreakers.
* **Comprehensive Interview Coordination**: Multi-round scheduling (`TECHNICAL_ROUND_1`, `TECHNICAL_ROUND_2`, `HR_ROUND`, `MANAGERIAL_ROUND`, `FINAL_ROUND`) with status tracking (`SCHEDULED`, `COMPLETED`, `PASSED`, `FAILED`).
* **Executive Reports**: Instant generation of system executive summaries, job-wise application analytics, and recruiter performance metrics.
* **Native Serialization**: Automatic binary persistence to `data/nexcruit_data.dat` with seed data on first run.

---

## 5. Integration of Java Course Concepts (Evaluation Component 4 – 6 Marks)

Nexcruit meaningfully integrates core Java OOP concepts:

| Java Concept | Concrete Implementation in Nexcruit | Location |
|---|---|---|
| **Classes & Objects** | High-fidelity domain entities (`Job`, `Candidate`, `Application`, `Interview`). | `src/auth/`, `src/jobs/`, `src/applications/`, `src/interviews/` |
| **Encapsulation** | Strict private fields, controlled getters/setters, and state mutation methods. | All model classes |
| **Inheritance** | Hierarchical specialization where `Candidate`, `Recruiter`, and `Admin` extend `User`. | `src/auth/` |
| **Abstraction & Abstract Classes** | Abstract class `User` declares the contract `public abstract void displayProfile()`. | [User.java](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/auth/User.java) |
| **Interfaces** | Interface `Matchable` specifies recruitment matching and scoring contracts. | [Matchable.java](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/matching/Matchable.java) |
| **Polymorphism** | Dynamic method dispatch during `user.displayProfile()`; reference `Matchable matchEngine = new MatchEngine()`. | [Main.java](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/Main.java) |
| **Method Overloading** | `Candidate.updateProfile(...)` overloaded with basic vs full skill signatures; `AuthManager.registerCandidate(...)` overloaded. | [Candidate.java](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/auth/Candidate.java), [AuthManager.java](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/auth/AuthManager.java) |
| **Method Overriding** | Subclasses override `displayProfile()`, `toString()`, and interface methods in `MatchEngine`. | Subclasses in `src/auth/`, `src/matching/` |
| **ArrayList** | Dynamic collections for jobs, applicant lists, skills, and ranked results. | `jobs`, `applications`, `matching` |
| **HashMap / LinkedHashMap** | Fast $O(1)$ lookup for user authentication, job lookups by ID, and application registries. | [AuthManager.java](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/auth/AuthManager.java), [JobManager.java](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/jobs/JobManager.java) |
| **HashSet** | Set intersection for case-insensitive technical skill matching. | [MatchEngine.java](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/matching/MatchEngine.java) |
| **Comparator** | Multi-level candidate ranking by match percentage descending, then CGPA descending. | [MatchEngine.java](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/matching/MatchEngine.java) |
| **Exception Handling** | Custom checked exceptions hierarchy: `DuplicateApplicationException`, `IneligibleCandidateException`, `InvalidInputException`, `UserNotFoundException`. | `src/exceptions/` |
| **Object Serialization** | All persistent classes implement `Serializable` with `private static final long serialVersionUID = 1L`. | `src/storage/`, model classes |
| **Enums** | Strongly typed lifecycle states: `ApplicationStatus`, `InterviewRound`, `InterviewStatus`. | `src/utils/`, `src/interviews/` |

---

## 6. Package and Project Structure

```
Nexcruit/
│
├── src/
│   ├── Main.java                          # Main entry point & Terminal Controller
│   │
│   ├── auth/                              # Module 1: Authentication & User Management
│   │   ├── User.java                      # Abstract base class
│   │   ├── Candidate.java                 # Candidate subclass
│   │   ├── Recruiter.java                 # Recruiter subclass
│   │   ├── Admin.java                     # Admin subclass
│   │   └── AuthManager.java               # Authentication service & credential registry
│   │
│   ├── jobs/                              # Module 2: Job Vacancy Management
│   │   ├── Job.java                       # Job vacancy entity
│   │   └── JobManager.java                # Job lifecycle manager
│   │
│   ├── applications/                      # Module 2: Application Pipeline Management
│   │   ├── Application.java               # Job application entity
│   │   └── ApplicationManager.java        # Application tracking & duplicate protection
│   │
│   ├── matching/                          # Module 3: Matching Engine (Focus Module)
│   │   ├── Matchable.java                 # Evaluation contract interface
│   │   ├── MatchEngine.java               # Matching algorithms & ranking logic
│   │   └── CandidateMatchResult.java      # Evaluation outcome data container
│   │
│   ├── interviews/                        # Module 3: Interview Lifecycle Management
│   │   ├── Interview.java                 # Scheduled interview entity
│   │   ├── InterviewManager.java          # Scheduling & evaluation manager
│   │   ├── InterviewRound.java            # Enum: Technical, HR, Managerial, Final
│   │   └── InterviewStatus.java           # Enum: Scheduled, Completed, Passed, Failed
│   │
│   ├── storage/                           # Module 4: File Handling & Serialization
│   │   ├── DataStore.java                 # Unified state envelope
│   │   └── FileManager.java               # ObjectOutputStream / ObjectInputStream handler
│   │
│   ├── reports/                           # Module 4: Analytics & Metrics Reporting
│   │   └── ReportManager.java             # System, job-wise, and recruiter reporting
│   │
│   ├── exceptions/                        # Custom Exception Hierarchy
│   │   ├── NexcruitException.java         # Base checked exception
│   │   ├── DuplicateApplicationException.java
│   │   ├── IneligibleCandidateException.java
│   │   ├── InvalidInputException.java
│   │   └── UserNotFoundException.java
│   │
│   ├── utils/                             # Helpers & Presentation Utilities
│   │   ├── ApplicationStatus.java         # Enum: Applied, Shortlisted, Selected, etc.
│   │   ├── ConsoleUI.java                 # Safe input readers & ANSI-style banners
│   │   └── InputValidator.java            # Format & boundary validators
│   │
│   └── test/                              # Automated Verification Suite
│       └── NexcruitTestSuite.java         # 43 automated unit & integration tests
│
├── bin/                                   # Compiled bytecode (.class files)
├── data/                                  # Serialized persistent storage (.dat files)
├── docs/                                  # Technical documentation & evaluation guides
│   ├── APPLICATION_FLOW.md
│   ├── CLASS_DESCRIPTIONS.md
│   ├── EVALUATION1_CHECKLIST.md
│   ├── MATCHING_ENGINE.md
│   └── MODULE_RESPONSIBILITIES.md
└── README.md
```

---

## 7. How to Compile and Run

The application runs directly in the VS Code Terminal using native PowerShell commands:

### Step 1: Open PowerShell in Workspace Root
Ensure you are in `c:\Users\galho\OneDrive\Desktop\Nexcruit`.

### Step 2: Compile All Java Source Files
```powershell
$env:PATH = "C:\Program Files\Java\jdk-26.0.1\bin;$env:PATH"
if (!(Test-Path bin)) { New-Item -ItemType Directory -Path bin }
$javaFiles = Get-ChildItem -Recurse -Path src -Filter *.java | Select-Object -ExpandProperty FullName
javac -d bin -cp src $javaFiles
```

### Step 3: Run the Automated Test Suite (Optional Verification)
```powershell
java -cp bin test.NexcruitTestSuite
```
*(Executes 43 tests verifying all 4 modules, matching algorithms, CGPA exclusions, and persistence with 100% pass rate).*

### Step 4: Run the Interactive Terminal Application
```powershell
java -cp bin Main
```

---

## 8. Default Pre-Configured Test Accounts

On first execution, Nexcruit automatically provisions starter demo accounts:

| Role | Email | Password | Details |
|---|---|---|---|
| **Administrator** | `admin@nexcruit.com` | `admin123` | System Administrator (Full platform visibility) |
| **Recruiter** | `sarah@techcorp.com` | `recruiter123` | TechCorp Solutions (Recruiter ID: `REC-101`) |
| **Candidate 1** | `alex@gmail.com` | `alex123` | Alex Rivera (B.Tech, CGPA: 8.4, Skills: Java, Spring Boot, SQL, Git, Docker) |
| **Candidate 2** | `priya@gmail.com` | `priya123` | Priya Sharma (BCA, CGPA: 6.8, Skills: Java, Python, HTML, CSS) |
| **Candidate 3** | `devon@gmail.com` | `devon123` | Devon Miller (M.Tech, CGPA: 9.1, Skills: Java, Spring Boot, SQL, Microservices) |

*You may also register fresh candidate or recruiter accounts at any time from the Main Menu.*

---

## 9. Sample Terminal Walkthrough

```text
================================================================================
   _  _ _____  ____ ____  _   _ ___ _____ 
  | \| | ____|/ ___|  _ \| | | |_ _|_   _|
  | .` |  _| | |   | |_) | | | || |  | |  
  |_|\_|_____|\____|_| \_\\___/|___| |_|  
       Smart Recruitment & Applicant Tracking System
================================================================================

--------------------------------------------------------------------------------
  >> MODULE 3 - MATCHING ENGINE EVALUATION
--------------------------------------------------------------------------------
Target Job: [JOB-101] Junior Java Backend Developer
Mandatory Min CGPA Requirement : 7.50
Required Skills                : Java, Spring Boot, SQL, Git
--------------------------------------------------------------------------------
EVALUATION RESULTS (Skill Matching Formula: Matched / Required * 100):
Cand ID    | Name               | CGPA   | Match %  | CGPA Cutoff | Matched Skills  | Missing Skills 
--------------------------------------------------------------------------------
CAN-101    | Alex Rivera        |   8.40 |  100.0% | PASS (>= 7.5) | Java,Spring ... |                
CAN-102    | Priya Sharma       |   6.80 |   25.0% | FAIL (< 7.5) | Java            | Spring Boot,...
--------------------------------------------------------------------------------
Do you want to auto-shortlist eligible candidates meeting a score threshold? (Y/N): Y
Enter minimum matching score threshold % [0.0 - 100.0]: 50.0
[SUCCESS] Auto-shortlisting complete!
Successfully shortlisted 1 eligible candidate(s).
Note: Candidates failing minimum CGPA (7.5) were automatically disqualified.
```

---

## 10. Current Implementation Status & Evaluation 1 Readiness

* [x] **Working Initial Prototype**: Complete, robust terminal UI without crashes.
* [x] **Module 1 (Auth & Users)**: Role-based access, inheritance, polymorphism, profile updating.
* [x] **Module 2 (Jobs & Applications)**: Full CRUD, active filtering, duplicate prevention.
* [x] **Module 3 (Matching Engine & Interviews)**: Fully implemented skills formula, separate CGPA filtering, comparator ranking, shortlisting, multi-round scheduling.
* [x] **Module 4 (File Handling & Reports)**: Object serialization, automatic seed creation, executive reporting.
* [x] **Evaluation 1 Checklist**: Complete mapping in `docs/EVALUATION1_CHECKLIST.md`.

---

## 11. Future Roadmap

1. **Resume Ingestion**: Integration of resume PDF/DOCX parsing via Apache Tika or PDFBox.
2. **Weighted Scoring GUI**: Activating the built-in multi-criteria weighted scoring engine through a JavaFX graphical interface.
3. **Automated Notifications**: Simulated SMTP email dispatcher for sending interview invites and offer letters.

# Sanchi's Module: File Handling & Reports (Evaluation 1)

**Module Owner**: Sanchi  
**Assigned Task**: File Handling & Reports (Evaluation 1 Scope: Java Object Serialization & Basic Recruitment Analytics)  
**Package**: `package nexcruit;`  
**Files Created**:
- `src/nexcruit/FileManager.java`
- `src/nexcruit/ReportManager.java`
- `src/nexcruit/FileReportTest.java`
- `docs/SANCHI_FILE_REPORT_MODULE.md`
- `sanchi/README.md`

---

## 1. Module Overview

In the **Nexcruit – Smart Recruitment & Applicant Tracking System**, Sanchi is responsible for:
1. **Persistent Data Storage (File Handling)**: Saving and restoring the system state across runs using standard Core Java Object Serialization and Deserialization into binary `.dat` files.
2. **Recruitment Analytics & Reports**: Calculating and displaying recruitment statistics dynamically from live system collections using simple loops, counters, and arithmetic formulas.

For **Evaluation 1**, the scope focuses on:
- Saving and loading users (`data/users.dat`), jobs (`data/jobs.dat`), and applications (`data/applications.dat`).
- Automatic data directory creation (`data/`).
- Robust exception handling (`FileNotFoundException`, `IOException`, `ClassNotFoundException`) with graceful missing-file fallback.
- Dynamic recruitment report generation (Total Jobs, Vacancies, Applications, Shortlisted, Selected, Rejected, Selection Ratio).
- Job-wise application breakdown table.

---

## 2. File Handling Architecture (Serialization & Deserialization)

### What is Object Serialization?
Object Serialization is a mechanism in Java where an object's state is converted into a byte stream. This byte stream can be written to a file on disk. When the program is restarted, deserialization converts the byte stream back into an identical Java object in memory.

### Files Managed in `data/`:
* `data/users.dat`: Stores `ArrayList<User>` (Candidate, Recruiter, Admin accounts).
* `data/jobs.dat`: Stores `ArrayList<Job>` (Job vacancies and criteria).
* `data/applications.dat`: Stores `ArrayList<Application>` (Applications linking candidates to jobs).

### Key Java Classes Used:
* `java.io.File`: Manages directories and checks whether files exist (`file.exists()`, `dir.mkdirs()`).
* `java.io.FileOutputStream` & `java.io.ObjectOutputStream`: Opens file byte streams and writes serialized object graphs via `oos.writeObject(...)`.
* `java.io.FileInputStream` & `java.io.ObjectInputStream`: Opens file byte streams and reads serialized object graphs via `(ArrayList<T>) ois.readObject()`.
* `java.io.Serializable`: Marker interface indicating that a class can be serialized.
* `serialVersionUID = 1L`: Ensures version compatibility between the serialized class and the running bytecode.

### Graceful Missing-File Handling:
When the application is run for the first time, no data files exist on disk. Instead of crashing with a `FileNotFoundException`, `FileManager` catches this condition, notifies the user, and returns an empty `ArrayList<T>()`. This allows the application to start cleanly.

---

## 3. Recruitment Analytics Architecture (`ReportManager.java`)

All statistical metrics are calculated dynamically using simple for-each loops and conditional statements—never hardcoded:

### Core Metrics & Formulas:
1. **Total Jobs**:
   $$\text{Total Jobs} = \text{jobManager.getAllJobs().size()}$$
2. **Total Vacancies**:
   $$\text{Total Vacancies} = \sum_{\text{job} \in \text{jobs}} \text{job.getNumberOfVacancies()}$$
3. **Total Applications**:
   $$\text{Total Applications} = \text{applicationManager.getAllApplications().size()}$$
4. **Status Counts**:
   Simple loop counting applications matching `"SHORTLISTED"`, `"SELECTED"`, `"REJECTED"`, or `"APPLIED"`.
5. **Selection Ratio**:
   $$\text{Selection Ratio} = \left(\frac{\text{Selected Candidates}}{\text{Total Applications}}\right) \times 100$$
   *Protection against division by zero*: If total applications is 0, the method returns `0.0%`.

---

## 4. Integration with Team Members

| Team Member | Module | Integration in `FileManager` / `ReportManager` |
|---|---|---|
| **Chesta** | User / Candidate Management | Reads `authManager.getAllUsers()`, serializes `Candidate`, `Recruiter`, `Admin`, restores via `authManager.restoreUsers(...)` |
| **Himani** | Job & Application Management | Reads `jobManager.getAllJobs()` and `applicationManager.getAllApplications()`, serializes `Job` and `Application`, restores via `restoreJobs(...)` and `restoreApplications(...)` |
| **Shanaya** | Matching Engine | Preserves candidate `matchScore` and `cgpaEligible` attributes serialized inside each `Application` object |

---

## 5. Verification & Test Results

The test suite [`src/nexcruit/FileReportTest.java`](file:///c:/Users/galho/OneDrive/Desktop/Nexcruit/src/nexcruit/FileReportTest.java) executes 12 comprehensive tests:

```text
================================================================================
         NEXCRUIT - SANCHI FILE HANDLING & REPORTS TEST SUITE (EVAL 1)          
================================================================================
Test 1 : Automatic data directory creation                       -> PASSED
Test 2 : Save and load registered users (users.dat)              -> PASSED
Test 3 : Save and load job records (jobs.dat)                    -> PASSED
Test 4 : Save and load applications with references (applications.dat) -> PASSED
Test 5 : Graceful handling of missing files (returns empty list) -> PASSED
Test 6 : Handle saving empty collections successfully            -> PASSED
Test 7 : Null safety protection for save operations              -> PASSED
Test 8 : Accurate recruitment counts (Jobs, Vacancies, Shortlisted, Selected, Rejected) -> PASSED
Test 9 : Selection ratio division-by-zero protection (returns 0.0%) -> PASSED
Test 10: Job-wise application statistics calculation             -> PASSED
Test 11: Application close & restart simulation (State persistence + ID sync) -> PASSED
Test 12: Terminal report outputs formatted without exceptions    -> PASSED
================================================================================
Total Tests Run: 12 | Passed: 12 | Failed: 0
Status: ALL TESTS PASSED SUCCESSFULLY (100%)
================================================================================
```

---

## 6. How to Compile and Run

From the project root:

```powershell
# Set Java in PATH
$env:PATH = "C:\Program Files\Java\jdk-26.0.1\bin;$env:PATH"

# Compile all files
$javaFiles = Get-ChildItem -Recurse -Path src, shanaya -Filter *.java | Select-Object -ExpandProperty FullName
javac -d bin -cp "src;shanaya" $javaFiles

# Run Sanchi's Test Suite
java -cp bin nexcruit.FileReportTest
```

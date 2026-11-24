# Shanaya's Module: Matching Engine (Evaluation 1)

**Module Owner**: Shanaya  
**Assigned Task**: Matching Engine & Interview Management (Evaluation 1 Scope: Basic Candidate Matching & CGPA Eligibility)  
**Package**: `package nexcruit;`  
**Folder Location**: `shanaya/`

---

## 1. Overview

This folder contains Shanaya's assigned module for **College Evaluation 1** of the **Nexcruit** project.
The Matching Engine evaluates how well an applicant fits a job opening through transparent, rule-based matching:
1. Calculates the percentage of required skills possessed by the candidate.
2. Checks whether the candidate meets the job's minimum academic CGPA requirement.
3. Handles real-world edge cases (case-insensitivity, extra whitespace, empty lists, and duplicate entries).
4. Associates the calculated score and eligibility status directly with the `Application` object for other team members (Sanchi's reports and Himani's applications).

> **Note on Evaluation 2 Scope**: Advanced candidate ranking with `Comparator`, weighted multi-criteria scoring, and interview scheduling (`Interview.java`, `InterviewManager.java`) are kept modular and will be integrated for Evaluation 2.

---

## 2. Files in this Folder

| File | Description |
|---|---|
| `Matchable.java` | Interface defining the contract for calculating skill matching score and checking CGPA eligibility. |
| `MatchEngine.java` | Core class implementing `Matchable` with the exact skills percentage formula and CGPA cutoff check. |
| `MatchingTest.java` | Verification test suite demonstrating all 10 required evaluation test cases. |
| `README.md` | This documentation file. |

---

## 3. Matching Formula and Calculation

The matching engine uses the standard rule-based formula:

$$\text{Matching Percentage} = \left(\frac{\text{Number of Matched Skills}}{\text{Total Required Skills}}\right) \times 100$$

### Example:
* **Job Required Skills**: `[Java, OOP, SQL, Data Structures]` (Total: 4)
* **Candidate Skills**: `[Java, OOP, SQL]` (Matched: 3)
* **Calculation**:
  $$\text{Matching Percentage} = \left(\frac{3}{4}\right) \times 100 = 75.0\%$$
* **CGPA Check**:
  $$\text{Candidate CGPA } (8.00) \ge \text{Job Minimum CGPA } (7.50) \implies \textbf{ELIGIBLE}$$

---

## 4. Edge Cases Handled

1. **Different Capitalization**: `"java"`, `"Java"`, and `"JAVA"` match properly using `equalsIgnoreCase()`.
2. **Extra Whitespace**: `"  Java  "` is cleaned using `.trim()`.
3. **Duplicate Skills**: `["Java", "Java", "SQL"]` is cleaned so duplicates are not counted twice.
4. **No Required Skills**: Returns `100.0%` because all 0 requirements are satisfied.
5. **Empty Candidate Skills**: Returns `0.0%` if requirements exist.
6. **Insufficient CGPA**: Returns `false` for CGPA eligibility even if skill match is 100%.

---

## 5. Integration with Team Members

* **Chesta's Module (`auth.Candidate`)**:
  - `MatchEngine` receives the actual `Candidate` object.
  - Reads skills with `candidate.getSkills()`.
  - Reads CGPA with `candidate.getCgpa()`.
* **Himani's Module (`nexcruit.Job` & `nexcruit.Application`)**:
  - `MatchEngine` receives the actual `Job` object.
  - Reads required skills with `job.getRequiredSkills()`.
  - Reads minimum CGPA with `job.getMinCgpa()`.
  - Updates `Application` with `application.setMatchScore(score)` and `application.setCgpaEligible(isEligible)`.
* **Sanchi's Module (`ReportManager`)**:
  - Can read `app.getMatchScore()` and `app.isCgpaEligible()` to compute average match percentages and eligibility statistics.

---

## 6. How to Compile and Run

From the root `Nexcruit` directory:

```powershell
# 1. Compile all Java files
$env:PATH = "C:\Program Files\Java\jdk-26.0.1\bin;$env:PATH"
if (!(Test-Path bin)) { New-Item -ItemType Directory -Path bin }
$javaFiles = Get-ChildItem -Recurse -Include *.java | Select-Object -ExpandProperty FullName
javac -d bin -cp "src;shanaya" $javaFiles

# 2. Run Shanaya's Matching Test Suite
java -cp bin nexcruit.MatchingTest
```

---

## 7. Test Results Summary

All 10 required test cases pass with 100% success rate:
- [x] Candidate with 100% skill match $\to$ 100.0%
- [x] Candidate with 75% skill match $\to$ 75.0%
- [x] Candidate with 50% skill match $\to$ 50.0%
- [x] Candidate with zero matching skills $\to$ 0.0%
- [x] Candidate with insufficient CGPA $\to$ Ineligible (false)
- [x] Candidate with sufficient CGPA $\to$ Eligible (true)
- [x] Empty required skills $\to$ 100.0%
- [x] Empty candidate skills $\to$ 0.0%
- [x] Skills with different capitalization $\to$ Case-insensitive match (100.0%)
- [x] Duplicate skills $\to$ Not double-counted

# Module 3 Guide: Matching Engine and Interview Management

> **Assigned to Shanaya**: This document provides a complete, beginner-friendly guide to explaining and demonstrating **Module 3: Matching Engine and Interview Management**.

---

## 1. What Does Module 3 Do?
Module 3 is the intelligence of Nexcruit. It answers two critical hiring questions:
1. **"How well does this applicant fit the job?"** (Matching Engine)
2. **"How do we evaluate and hire shortlisted applicants?"** (Interview Management)

---

## 2. Key Classes in Module 3

| Class / Interface | Location | What it does |
|---|---|---|
| `Matchable` | `src/matching/Matchable.java` | Interface defining what evaluation methods exist. |
| `MatchEngine` | `src/matching/MatchEngine.java` | Core class that calculates skill %, checks CGPA, ranks candidates, and shortlists them. |
| `CandidateMatchResult` | `src/matching/CandidateMatchResult.java` | Holds the evaluation result for one candidate (score, eligibility, matched/missing skills). |
| `InterviewRound` | `src/interviews/InterviewRound.java` | Enum for interview stages (Technical 1, Technical 2, HR, Managerial, Final). |
| `InterviewStatus` | `src/interviews/InterviewStatus.java` | Enum for interview status (Scheduled, Completed, Passed, Failed). |
| `Interview` | `src/interviews/Interview.java` | Entity representing one scheduled interview round. |
| `InterviewManager` | `src/interviews/InterviewManager.java` | Manages scheduling, viewing, and updating interview results. |

---

## 3. Detailed Logic & Formulas

### A. Skills Matching Formula
```java
// Location: MatchEngine.java -> calculateMatchScore()
double percentage = ((double) matchedCount / totalRequired) * 100.0;
```
* **Example**:
  * Job requires 4 skills: `[Java, Spring Boot, SQL, Git]`
  * Alex Rivera has: `[Java, Spring Boot, SQL, Git, Docker]`
  * Matched skills: `4` $\to (4 / 4) \times 100 = 100.0\%$
  * Priya Sharma has: `[Java, Python, HTML, CSS]`
  * Matched skills: `1` (Java) $\to (1 / 4) \times 100 = 25.0\%$

### B. Separate CGPA Eligibility Check
```java
// Location: MatchEngine.java -> checkCgpaEligibility()
if (candidate.getCgpa() >= job.getMinCgpa()) {
    return true;  // Eligible
} else {
    return false; // Ineligible
}
```
* **Strict Rule**:
  Even if Priya had 100% skill match, her CGPA is `6.80`. If the job requires a minimum CGPA of `7.50`, she is **ineligible**.

### C. Auto-Shortlisting Logic
```java
// Location: MatchEngine.java -> shortlistCandidates()
for (CandidateMatchResult result : rankedResults) {
    if (!result.isCgpaEligible()) {
        // Disqualify and mark as REJECTED
        applicationManager.updateStatus(appId, ApplicationStatus.REJECTED, "Disqualified: CGPA below cutoff");
        continue; // DO NOT SHORTLIST!
    }

    if (result.getMatchPercentage() >= cutoffScore) {
        // Successfully shortlist
        applicationManager.updateStatus(appId, ApplicationStatus.SHORTLISTED, "Shortlisted: Score >= threshold");
        shortlisted.add(result);
    }
}
```

### D. Candidate Ranking Logic
We rank candidates using a straightforward `Comparator`:
1. Eligible candidates appear before Ineligible candidates.
2. Higher Match Score appears before Lower Match Score.
3. Higher CGPA appears first if Match Scores are equal.
4. Alphabetical by Name if both Score and CGPA are equal.

---

## 4. How Module 3 Connects with Team Members' Modules

1. **Connection with Module 1 (Authentication - Candidates & Recruiters)**:
   - When `MatchEngine` evaluates candidates, it fetches the `Candidate` object from `AuthManager` using `authManager.getUserById(candidateId)`.
   - It reads `candidate.getSkills()` and `candidate.getCgpa()`.

2. **Connection with Module 2 (Jobs & Applications)**:
   - It reads `job.getRequiredSkills()` and `job.getMinCgpa()`.
   - It updates the `Application` status in `ApplicationManager` from `APPLIED` to `SHORTLISTED` or `REJECTED`.

3. **Connection with Module 4 (File Handling & Reports)**:
   - All interviews and updated application scores are serialized by `FileManager`.
   - `ReportManager` reads the shortlisted and interview statistics to display executive summaries.

# NEXCRUIT – College Viva Questions and Answers

This document prepares you for any question your professor might ask during the Java OOP evaluation. Every answer is written in plain, beginner-friendly English based on the exact code in your repository.

---

## Part 1: Core OOP Concepts Viva Questions

### Q1: What is Object-Oriented Programming (OOP) and how is it used in Nexcruit?
**Answer**:
"OOP is a programming approach organized around objects rather than just functions. In Nexcruit, we model real-world recruitment entities as classes:
- A job seeker is modeled as a `Candidate` object.
- A job posting is modeled as a `Job` object.
- An application linking the candidate and job is an `Application` object.
- An interview round is an `Interview` object."

---

### Q2: How did you implement Abstraction in your project?
**Answer**:
"We implemented abstraction using the abstract class `User` and the interface `Matchable`:
1. `User` is an `abstract class` because we never want to create a generic 'User'. A user must be a specific type: either a `Candidate`, `Recruiter`, or `Admin`. We declared an abstract method `public abstract void displayProfile();`.
2. `Matchable` is an `interface` that defines what methods the matching engine must have (`calculateMatchScore`, `checkCgpaEligibility`), without specifying the exact formula. The `MatchEngine` class then implements these methods."

---

### Q3: What is Encapsulation and where did you use it?
**Answer**:
"Encapsulation is bundling data (variables) and methods that operate on that data inside a single unit (class), while hiding internal details using the `private` access modifier.
In classes like `Job`, `Candidate`, `Application`, and `Interview`, all fields (like `cgpa`, `password`, `matchScore`) are `private`. Other classes cannot directly tamper with them. They must use public getter and setter methods like `getCgpa()` and `setCgpa()`. This protects data integrity."

---

### Q4: How is Inheritance used in Nexcruit?
**Answer**:
"We used single-level inheritance in our `auth` package:
- `User` is the parent superclass containing common fields: `userId`, `name`, `email`, `password`, and `role`.
- `Candidate extends User`: Adds candidate-specific fields like `cgpa`, `education`, and `skills`.
- `Recruiter extends User`: Adds recruiter-specific fields like `companyName` and `department`.
- `Admin extends User`: Represents system administrator.
By using `extends` and calling `super(...)` in the subclass constructor, we reuse code and avoid writing `name`, `email`, and `password` three times."

---

### Q5: What is Polymorphism and how does it work in your code?
**Answer**:
"Polymorphism means 'many forms'. We demonstrate both runtime and compile-time polymorphism:
1. **Runtime Polymorphism (Method Overriding)**: The abstract method `displayProfile()` in `User` is overridden in `Candidate`, `Recruiter`, and `Admin`. When `user.displayProfile()` is called, Java dynamically calls the candidate version for candidates and recruiter version for recruiters.
2. **Compile-time Polymorphism (Method Overloading)**: In `Candidate`, we wrote two versions of `updateProfile()`: one that updates basic info, and an overloaded one that also takes a new `ArrayList<String> skills` list."

---

### Q6: What is the difference between an Abstract Class and an Interface?
**Answer**:
"- **Abstract Class (`User`)**: Can have state (instance variables like `name`, `email`), constructors, and concrete methods, as well as abstract methods. A subclass can only extend one abstract class.
- **Interface (`Matchable`)**: Defines a contract with method signatures. A class can implement multiple interfaces. We used `Matchable` to separate the calculation specifications from the `MatchEngine` implementation."

---

## Part 2: Module 3 Viva Questions (Matching Engine & Interviews)

> **Important**: This is your personally assigned module. Prepare these questions thoroughly!

### Q7: What is the formula for the Skills Matching Percentage and where is it written?
**Answer**:
"The formula is implemented in `MatchEngine.java` in the method `calculateMatchScore(Candidate candidate, Job job)`:
$$\text{Matching Percentage} = \left(\frac{\text{Number of Matched Skills}}{\text{Total Required Skills}}\right) \times 100$$
We loop through each required skill of the job and check if the candidate's skills list contains it using `equalsIgnoreCase()`. We count how many skills match, divide by total required skills, and multiply by 100.0."

---

### Q8: How does your system ensure candidates who fail the minimum CGPA are not shortlisted?
**Answer**:
"In `MatchEngine.java`, we intentionally decoupled the CGPA eligibility check from the skills score calculation:
1. The method `checkCgpaEligibility(candidate, job)` checks if `candidate.getCgpa() >= job.getMinCgpa()`.
2. In `shortlistCandidates()`, the first line inside the loop is:
   ```java
   if (!result.isCgpaEligible()) {
       applicationManager.updateStatus(appId, ApplicationStatus.REJECTED, ...);
       continue; // Skips this candidate!
   }
   ```
Even if a candidate has 100% skill match, if their CGPA is below the job's minimum requirement, they are disqualified and marked `REJECTED`. They are never shortlisted."

---

### Q9: How does your candidate ranking work without using complex lambdas?
**Answer**:
"In `MatchEngine.java`, we use `Collections.sort()` with a simple custom `Comparator<CandidateMatchResult>`. Inside the `compare` method, we wrote straightforward `if-else` blocks:
1. First, we compare eligibility: if Candidate 1 is eligible and Candidate 2 is not, Candidate 1 comes first (`return -1`).
2. Next, we compare `matchPercentage`: higher percentage comes first (`return -1`).
3. If scores are tied, higher `CGPA` comes first.
4. If still tied, we sort alphabetically by candidate name using `compareToIgnoreCase()`."

---

### Q10: How do you schedule interviews and associate them with candidates and jobs?
**Answer**:
"In `InterviewManager.java`, the `scheduleInterview()` method takes the `applicationId`, `candidateId`, `jobId`, `recruiterId`, `round`, and date/time. It creates a new `Interview` object with an auto-incremented ID like `INT-101` and stores it in a `LinkedHashMap`. It also automatically updates the application's status to `ApplicationStatus.INTERVIEW_SCHEDULED`."

---

## Part 3: Collections and File Handling Viva Questions

### Q11: Why did you use `ArrayList` and `HashMap` instead of plain arrays?
**Answer**:
"- **Arrays** have a fixed size that cannot change once created.
- **`ArrayList`** grows and shrinks dynamically as candidates apply or jobs are created (`list.add(...)`, `list.size()`).
- **`HashMap`** allows instant $O(1)$ lookup using a key (like looking up a user by `email` or looking up a job by `jobId`), avoiding slow linear searches."

---

### Q12: How does data persistence work without MySQL or external databases?
**Answer**:
"We used native Java **Object Serialization**:
- Our data models (`User`, `Job`, `Application`, `Interview`, `DataStore`) implement `java.io.Serializable`.
- In `FileManager.java`, we use `FileOutputStream` and `ObjectOutputStream` to write the entire `DataStore` object into a binary file `data/nexcruit_data.dat`.
- When the program starts, `FileInputStream` and `ObjectInputStream` read the object back into memory. This saves and restores all data between runs."

---

### Q13: What is `serialVersionUID` and why is it defined in each serializable class?
**Answer**:
"`serialVersionUID` is a unique version identifier for a Serializable class. Java uses it during deserialization to verify that the loaded object from the file matches the class definition currently in memory. Defining `private static final long serialVersionUID = 1L;` prevents unexpected `InvalidClassException` errors."

---

### Q14: How does your code prevent duplicate job applications?
**Answer**:
"In `ApplicationManager.java`, before creating an application, `hasCandidateApplied(candidateId, jobId)` loops through existing applications. If an application with the same `candidateId` and `jobId` already exists, we throw a custom exception: `DuplicateApplicationException`. In `Main.java`, we catch this exception and display a friendly warning message instead of letting the program crash."

---

### Q15: How does your program handle invalid user inputs in the terminal?
**Answer**:
"In `ConsoleUI.java`, we created helper methods like `readInt` and `readDouble`. Inside these methods, we read the input line with `scanner.nextLine()` and parse it inside a `try-catch` block:
```java
try {
    int value = Integer.parseInt(input);
    return value;
} catch (NumberFormatException e) {
    System.out.println('Please enter digits only.');
}
```
If the user types letters instead of numbers, `NumberFormatException` is caught, an error message is printed, and the loop repeats. The application never crashes."

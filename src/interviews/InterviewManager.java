package interviews;

import exceptions.InvalidInputException;
import utils.InputValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Manages scheduling and updating interviews for shortlisted candidates.
 * Concepts used:
 * - LinkedHashMap and ArrayList
 * - Custom Exceptions (InvalidInputException)
 * - Simple loops and if-else logic
 */
public class InterviewManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private LinkedHashMap<String, Interview> interviewsMap;
    private int interviewCounter;

    public InterviewManager() {
        this.interviewsMap = new LinkedHashMap<String, Interview>();
        this.interviewCounter = 101;
    }

    // Schedules a new interview round
    public Interview scheduleInterview(String applicationId, String candidateId, String jobId,
                                       String recruiterId, InterviewRound round,
                                       String scheduledDateTime, String mode)
            throws InvalidInputException {

        if (!InputValidator.isNonEmpty(applicationId)) {
            throw new InvalidInputException("Application ID cannot be empty.");
        }
        if (!InputValidator.isNonEmpty(candidateId)) {
            throw new InvalidInputException("Candidate ID cannot be empty.");
        }
        if (!InputValidator.isNonEmpty(jobId)) {
            throw new InvalidInputException("Job ID cannot be empty.");
        }
        if (round == null) {
            throw new InvalidInputException("Interview round must be selected.");
        }
        if (!InputValidator.isNonEmpty(scheduledDateTime)) {
            throw new InvalidInputException("Scheduled date and time cannot be empty.");
        }

        String interviewId = "INT-" + interviewCounter;
        interviewCounter++;

        Interview interview = new Interview(interviewId, applicationId.trim().toUpperCase(),
                candidateId.trim().toUpperCase(), jobId.trim().toUpperCase(),
                recruiterId != null ? recruiterId.trim().toUpperCase() : "REC-UNKNOWN",
                round, scheduledDateTime.trim(), mode);

        interviewsMap.put(interviewId, interview);
        return interview;
    }

    // Lookup interview by ID
    public Interview getInterviewById(String interviewId) {
        if (interviewId == null) return null;
        return interviewsMap.get(interviewId.trim().toUpperCase());
    }

    // Get all interviews for a specific candidate
    public ArrayList<Interview> getInterviewsByCandidate(String candidateId) {
        ArrayList<Interview> list = new ArrayList<Interview>();
        if (candidateId == null) return list;

        for (Interview i : interviewsMap.values()) {
            if (candidateId.equalsIgnoreCase(i.getCandidateId())) {
                list.add(i);
            }
        }
        return list;
    }

    // Get all interviews conducted by a specific recruiter
    public ArrayList<Interview> getInterviewsByRecruiter(String recruiterId) {
        ArrayList<Interview> list = new ArrayList<Interview>();
        if (recruiterId == null) return list;

        for (Interview i : interviewsMap.values()) {
            if (recruiterId.equalsIgnoreCase(i.getRecruiterId())) {
                list.add(i);
            }
        }
        return list;
    }

    // Updates interview result status (e.g. PASSED, FAILED) and interviewer feedback
    public boolean updateInterviewStatus(String interviewId, InterviewStatus newStatus, String feedback) {
        Interview interview = getInterviewById(interviewId);
        if (interview != null && newStatus != null) {
            interview.setStatus(newStatus);
            if (feedback != null && !feedback.trim().isEmpty()) {
                interview.setFeedback(feedback.trim());
            }
            return true;
        }
        return false;
    }

    // Returns all interviews
    public ArrayList<Interview> getAllInterviews() {
        return new ArrayList<Interview>(interviewsMap.values());
    }

    // Restores interviews from file
    public void restoreInterviews(Collection<Interview> loadedInterviews) {
        interviewsMap.clear();
        if (loadedInterviews == null) return;

        for (Interview i : loadedInterviews) {
            interviewsMap.put(i.getInterviewId(), i);
            try {
                String[] parts = i.getInterviewId().split("-");
                if (parts.length == 2) {
                    int num = Integer.parseInt(parts[1]);
                    if (num >= interviewCounter) {
                        interviewCounter = num + 1;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
}

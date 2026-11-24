package storage;

import applications.Application;
import auth.User;
import interviews.Interview;
import jobs.Job;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Composite container object that holds all system data to be saved to disk.
 * Concepts used:
 * - ArrayList
 * - Serializable
 */
public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<User> users;
    private ArrayList<Job> jobs;
    private ArrayList<Application> applications;
    private ArrayList<Interview> interviews;

    public DataStore() {
        this.users = new ArrayList<User>();
        this.jobs = new ArrayList<Job>();
        this.applications = new ArrayList<Application>();
        this.interviews = new ArrayList<Interview>();
    }

    public DataStore(ArrayList<User> users, ArrayList<Job> jobs,
                     ArrayList<Application> applications, ArrayList<Interview> interviews) {
        this.users = (users != null) ? users : new ArrayList<User>();
        this.jobs = (jobs != null) ? jobs : new ArrayList<Job>();
        this.applications = (applications != null) ? applications : new ArrayList<Application>();
        this.interviews = (interviews != null) ? interviews : new ArrayList<Interview>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public void setApplications(ArrayList<Application> applications) {
        this.applications = applications;
    }

    public ArrayList<Interview> getInterviews() {
        return interviews;
    }

    public void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }
}

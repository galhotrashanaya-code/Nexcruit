package nexcruit;

import auth.AuthManager;
import auth.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Handles File Handling and Object Serialization/Deserialization for Nexcruit.
 * Developed by: Sanchi (File Handling & Reports Module - Evaluation 1)
 *
 * Concepts used:
 * - File Handling (java.io.File, mkdirs(), exists())
 * - Object Serialization (ObjectOutputStream, writeObject())
 * - Object Deserialization (ObjectInputStream, readObject())
 * - Exception Handling (FileNotFoundException, IOException, ClassNotFoundException)
 * - Collections (ArrayList)
 * - Beginner-friendly control flow and null safety
 */
public class FileManager implements Serializable {
    private static final long serialVersionUID = 1L;

    // Default data directory and file paths
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + File.separator + "users.dat";
    private static final String JOBS_FILE = DATA_DIR + File.separator + "jobs.dat";
    private static final String APPLICATIONS_FILE = DATA_DIR + File.separator + "applications.dat";

    /**
     * Default Constructor:
     * Automatically verifies and creates the data directory if it does not already exist.
     */
    public FileManager() {
        ensureDataDirectoryExists();
    }

    /**
     * Ensures the data/ directory exists.
     * Creates parent and child directories if missing.
     */
    public void ensureDataDirectoryExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("[FileManager] Data directory created successfully: " + dir.getAbsolutePath());
            }
        }
    }

    // =========================================================================
    // USER PERSISTENCE (users.dat)
    // =========================================================================

    /**
     * Saves user accounts (Candidates, Recruiters, Admins) to data/users.dat using Serialization.
     *
     * @param users The list of User objects to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveUsers(ArrayList<User> users) {
        ensureDataDirectoryExists();
        if (users == null) {
            System.out.println("[FileManager] Warning: Cannot save null users list.");
            return false;
        }

        File file = new File(USERS_FILE);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.flush();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("[FileManager] Error: Users file not found or cannot be created: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving users to " + USERS_FILE + ": " + e.getMessage());
            return false;
        } finally {
            // Close streams safely
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                System.out.println("[FileManager] Error closing user output stream: " + e.getMessage());
            }
        }
    }

    /**
     * Loads user accounts from data/users.dat using Deserialization.
     * Handles missing files gracefully by returning an empty list instead of throwing an error.
     *
     * @return ArrayList of restored User objects (or empty list if file doesn't exist)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<User> loadUsers() {
        File file = new File(USERS_FILE);

        // Graceful handling of missing files
        if (!file.exists()) {
            System.out.println("[FileManager] Notice: " + USERS_FILE + " does not exist yet. Starting with empty users list.");
            return new ArrayList<User>();
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>) {
                return (ArrayList<User>) obj;
            }
            return new ArrayList<User>();
        } catch (FileNotFoundException e) {
            System.out.println("[FileManager] Notice: " + USERS_FILE + " not found: " + e.getMessage());
            return new ArrayList<User>();
        } catch (ClassNotFoundException e) {
            System.out.println("[FileManager] Error: User class definition not found during deserialization: " + e.getMessage());
            return new ArrayList<User>();
        } catch (IOException e) {
            System.out.println("[FileManager] Error reading users from " + USERS_FILE + ": " + e.getMessage());
            return new ArrayList<User>();
        } finally {
            // Close streams safely
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.out.println("[FileManager] Error closing user input stream: " + e.getMessage());
            }
        }
    }

    // =========================================================================
    // JOB PERSISTENCE (jobs.dat)
    // =========================================================================

    /**
     * Saves job postings to data/jobs.dat using Serialization.
     *
     * @param jobs The list of Job objects to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveJobs(ArrayList<Job> jobs) {
        ensureDataDirectoryExists();
        if (jobs == null) {
            System.out.println("[FileManager] Warning: Cannot save null jobs list.");
            return false;
        }

        File file = new File(JOBS_FILE);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(jobs);
            oos.flush();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("[FileManager] Error: Jobs file not found or cannot be created: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving jobs to " + JOBS_FILE + ": " + e.getMessage());
            return false;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                System.out.println("[FileManager] Error closing job output stream: " + e.getMessage());
            }
        }
    }

    /**
     * Loads job postings from data/jobs.dat using Deserialization.
     *
     * @return ArrayList of restored Job objects (or empty list if file doesn't exist)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Job> loadJobs() {
        File file = new File(JOBS_FILE);

        if (!file.exists()) {
            System.out.println("[FileManager] Notice: " + JOBS_FILE + " does not exist yet. Starting with empty jobs list.");
            return new ArrayList<Job>();
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>) {
                return (ArrayList<Job>) obj;
            }
            return new ArrayList<Job>();
        } catch (FileNotFoundException e) {
            System.out.println("[FileManager] Notice: " + JOBS_FILE + " not found: " + e.getMessage());
            return new ArrayList<Job>();
        } catch (ClassNotFoundException e) {
            System.out.println("[FileManager] Error: Job class definition not found during deserialization: " + e.getMessage());
            return new ArrayList<Job>();
        } catch (IOException e) {
            System.out.println("[FileManager] Error reading jobs from " + JOBS_FILE + ": " + e.getMessage());
            return new ArrayList<Job>();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.out.println("[FileManager] Error closing job input stream: " + e.getMessage());
            }
        }
    }

    // =========================================================================
    // APPLICATION PERSISTENCE (applications.dat)
    // =========================================================================

    /**
     * Saves candidate applications to data/applications.dat using Serialization.
     * Candidate and Job references aggregated inside Application are serialized automatically.
     *
     * @param applications The list of Application objects to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveApplications(ArrayList<Application> applications) {
        ensureDataDirectoryExists();
        if (applications == null) {
            System.out.println("[FileManager] Warning: Cannot save null applications list.");
            return false;
        }

        File file = new File(APPLICATIONS_FILE);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(applications);
            oos.flush();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("[FileManager] Error: Applications file not found or cannot be created: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving applications to " + APPLICATIONS_FILE + ": " + e.getMessage());
            return false;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                System.out.println("[FileManager] Error closing application output stream: " + e.getMessage());
            }
        }
    }

    /**
     * Loads candidate applications from data/applications.dat using Deserialization.
     *
     * @return ArrayList of restored Application objects (or empty list if file doesn't exist)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Application> loadApplications() {
        File file = new File(APPLICATIONS_FILE);

        if (!file.exists()) {
            System.out.println("[FileManager] Notice: " + APPLICATIONS_FILE + " does not exist yet. Starting with empty applications list.");
            return new ArrayList<Application>();
        }

        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>) {
                return (ArrayList<Application>) obj;
            }
            return new ArrayList<Application>();
        } catch (FileNotFoundException e) {
            System.out.println("[FileManager] Notice: " + APPLICATIONS_FILE + " not found: " + e.getMessage());
            return new ArrayList<Application>();
        } catch (ClassNotFoundException e) {
            System.out.println("[FileManager] Error: Application class definition not found during deserialization: " + e.getMessage());
            return new ArrayList<Application>();
        } catch (IOException e) {
            System.out.println("[FileManager] Error reading applications from " + APPLICATIONS_FILE + ": " + e.getMessage());
            return new ArrayList<Application>();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.out.println("[FileManager] Error closing application input stream: " + e.getMessage());
            }
        }
    }

    // =========================================================================
    // MANAGER INTEGRATION METHODS (Unified Save/Load for Main.java)
    // =========================================================================

    /**
     * Saves all system data (users, jobs, and applications) in one call.
     * Ideal for calling on application shutdown or logout in Main.java.
     *
     * @param authManager The AuthManager containing users
     * @param jobManager The JobManager containing jobs
     * @param applicationManager The ApplicationManager containing applications
     * @return true if all three files were saved successfully, false otherwise
     */
    public boolean saveAll(AuthManager authManager, JobManager jobManager, ApplicationManager applicationManager) {
        boolean usersSaved = false;
        boolean jobsSaved = false;
        boolean appsSaved = false;

        if (authManager != null) {
            usersSaved = saveUsers(authManager.getAllUsers());
        }
        if (jobManager != null) {
            jobsSaved = saveJobs(jobManager.getAllJobs());
        }
        if (applicationManager != null) {
            appsSaved = saveApplications(applicationManager.getAllApplications());
        }

        return usersSaved && jobsSaved && appsSaved;
    }

    /**
     * Loads all system data from disk and restores each manager's state.
     * Ideal for calling during application startup in Main.java.
     *
     * @param authManager The AuthManager to restore
     * @param jobManager The JobManager to restore
     * @param applicationManager The ApplicationManager to restore
     * @return true when loading finishes
     */
    public boolean loadAll(AuthManager authManager, JobManager jobManager, ApplicationManager applicationManager) {
        if (authManager != null) {
            ArrayList<User> loadedUsers = loadUsers();
            authManager.restoreUsers(loadedUsers);
        }

        if (jobManager != null) {
            ArrayList<Job> loadedJobs = loadJobs();
            jobManager.restoreJobs(loadedJobs);
        }

        if (applicationManager != null) {
            ArrayList<Application> loadedApps = loadApplications();
            applicationManager.restoreApplications(loadedApps);
        }

        return true;
    }

    /**
     * Checks if persistent data files currently exist on disk.
     *
     * @return true if any of the three data files exist
     */
    public boolean dataExists() {
        File uFile = new File(USERS_FILE);
        File jFile = new File(JOBS_FILE);
        File aFile = new File(APPLICATIONS_FILE);
        return uFile.exists() || jFile.exists() || aFile.exists();
    }

    /**
     * Deletes saved data files. Useful for resetting or running fresh tests.
     */
    public boolean clearAllData() {
        boolean uDeleted = new File(USERS_FILE).delete();
        boolean jDeleted = new File(JOBS_FILE).delete();
        boolean aDeleted = new File(APPLICATIONS_FILE).delete();
        return uDeleted || jDeleted || aDeleted;
    }

    // Getters for file paths (for testing / inspection)
    public String getUsersFilePath() {
        return USERS_FILE;
    }

    public String getJobsFilePath() {
        return JOBS_FILE;
    }

    public String getApplicationsFilePath() {
        return APPLICATIONS_FILE;
    }
}

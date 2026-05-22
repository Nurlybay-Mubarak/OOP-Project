package storage;

import model.academic.Course;
import model.communication.Message;
import model.communication.News;
import model.research.Journal;
import model.research.ResearchPaper;
import model.support.SupportRequest;
import model.users.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton in-memory data store for the entire UniversitySystem.
 * Acts as the system's "database" — all controllers read and write through this class.
 *
 * Pattern: Singleton (thread-safe via synchronized getInstance()).
 */
public class DataStore implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "university_data.ser";

    // ------------------------------------------------------------------ //
    //  Singleton
    // ------------------------------------------------------------------ //

    private static DataStore instance;

    private DataStore() {
        users             = new ArrayList<>();
        courses           = new ArrayList<>();
        newsList          = new ArrayList<>();
        supportRequests   = new ArrayList<>();
        papers            = new ArrayList<>();
        journals          = new ArrayList<>();
        messages          = new ArrayList<>();
        organizations     = new ArrayList<>();
        researchProjects  = new ArrayList<>();
    }

    /**
     * Returns the single shared instance of DataStore.
     * Lazy-initialised, thread-safe.
     */
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // ------------------------------------------------------------------ //
    //  In-Memory Collections
    // ------------------------------------------------------------------ //

    private List<User>               users;
    private List<Course>              courses;
    private List<News>                newsList;
    private List<SupportRequest>      supportRequests;
    private List<ResearchPaper>       papers;
    private List<Journal>             journals;
    private List<Message>             messages;
    private List<model.academic.StudentOrganization>  organizations;
    private List<model.research.ResearchProject>      researchProjects;

    // ------------------------------------------------------------------ //
    //  User CRUD
    // ------------------------------------------------------------------ //

    public void addUser(User u) {
        if (u != null && !users.contains(u)) users.add(u);
    }

    public void removeUser(User u) {
        users.remove(u);
    }

    public void updateUser(User u) {
        // Object reference is already updated in-memory; no extra action needed.
    }

    public Optional<User> findUserByLogin(String login) {
        return users.stream().filter(u -> u.getLogin().equals(login)).findFirst();
    }

    public Optional<User> findUserByLoginAndPassword(String login, String password) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login) && u.checkPassword(password))
                .findFirst();
    }

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<Student> getAllStudents() {
        return users.stream()
                .filter(u -> u instanceof Student && !(u instanceof GraduateStudent))
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }

    public List<GraduateStudent> getAllGraduateStudents() {
        return users.stream()
                .filter(u -> u instanceof GraduateStudent)
                .map(u -> (GraduateStudent) u)
                .collect(Collectors.toList());
    }

    public List<Teacher> getAllTeachers() {
        return users.stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .collect(Collectors.toList());
    }

    public List<Manager> getAllManagers() {
        return users.stream()
                .filter(u -> u instanceof Manager)
                .map(u -> (Manager) u)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------ //
    //  Course CRUD
    // ------------------------------------------------------------------ //

    public void addCourse(Course c) {
        if (c != null && !courses.contains(c)) courses.add(c);
    }

    public void removeCourse(Course c) {
        courses.remove(c);
    }

    public Optional<Course> findCourseByCode(String code) {
        return courses.stream().filter(c -> c.getCode().equals(code)).findFirst();
    }

    public List<Course> getAllCourses() {
        return Collections.unmodifiableList(courses);
    }

    // ------------------------------------------------------------------ //
    //  News CRUD
    // ------------------------------------------------------------------ //

    public void addNews(News n) {
        if (n != null && !newsList.contains(n)) newsList.add(n);
    }

    public List<News> getAllNews() {
        return Collections.unmodifiableList(newsList);
    }

    public List<News> getPinnedNews() {
        return newsList.stream().filter(News::isPinned).collect(Collectors.toList());
    }

    // ------------------------------------------------------------------ //
    //  SupportRequest CRUD
    // ------------------------------------------------------------------ //

    public void addSupportRequest(SupportRequest r) {
        if (r != null && !supportRequests.contains(r)) supportRequests.add(r);
    }

    public List<SupportRequest> getAllSupportRequests() {
        return Collections.unmodifiableList(supportRequests);
    }

    // ------------------------------------------------------------------ //
    //  ResearchPaper CRUD
    // ------------------------------------------------------------------ //

    public void addResearchPaper(ResearchPaper p) {
        if (p != null && !papers.contains(p)) papers.add(p);
    }

    public List<ResearchPaper> getAllResearchPapers() {
        return Collections.unmodifiableList(papers);
    }

    // ------------------------------------------------------------------ //
    //  Journal CRUD
    // ------------------------------------------------------------------ //

    public void addJournal(Journal j) {
        if (j != null && !journals.contains(j)) journals.add(j);
    }

    public List<Journal> getAllJournals() {
        return Collections.unmodifiableList(journals);
    }

    public Optional<Journal> findJournalByName(String name) {
        return journals.stream().filter(j -> j.getName().equals(name)).findFirst();
    }

    // ------------------------------------------------------------------ //
    //  Message storage
    // ------------------------------------------------------------------ //

    public void addMessage(Message m) {
        if (m != null) messages.add(m);
    }

    public List<Message> getAllMessages() {
        return Collections.unmodifiableList(messages);
    }

    // ------------------------------------------------------------------ //
    //  StudentOrganization storage
    // ------------------------------------------------------------------ //

    public void addOrganization(model.academic.StudentOrganization org) {
        if (org != null && !organizations.contains(org)) organizations.add(org);
    }

    public List<model.academic.StudentOrganization> getAllOrganizations() {
        return Collections.unmodifiableList(organizations);
    }

    // ------------------------------------------------------------------ //
    //  ResearchProject storage
    // ------------------------------------------------------------------ //

    public void addResearchProject(model.research.ResearchProject rp) {
        if (rp != null && !researchProjects.contains(rp)) researchProjects.add(rp);
    }

    public List<model.research.ResearchProject> getAllResearchProjects() {
        return Collections.unmodifiableList(researchProjects);
    }

    // ------------------------------------------------------------------ //
    //  Serialization (Data Persistence)
    // ------------------------------------------------------------------ //

    /**
     * Save the current DataStore state to a file using Java serialization.
     * Writes all in-memory collections to disk so they survive restarts.
     */
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println("[DATASTORE] Data saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("[DATASTORE] Save failed: " + e.getMessage());
        }
    }

    /**
     * Load a previously saved DataStore from file.
     * Restores all collections to their saved state.
     *
     * @return true if data was loaded successfully, false otherwise
     */
    public boolean loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("[DATASTORE] No saved data found.");
            return false;
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            DataStore loaded = (DataStore) ois.readObject();
            this.users            = loaded.users;
            this.courses          = loaded.courses;
            this.newsList         = loaded.newsList;
            this.supportRequests  = loaded.supportRequests;
            this.papers           = loaded.papers;
            this.journals         = loaded.journals;
            this.messages         = loaded.messages;
            this.organizations    = loaded.organizations;
            this.researchProjects = loaded.researchProjects;
            System.out.println("[DATASTORE] Data loaded from " + DATA_FILE);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[DATASTORE] Load failed: " + e.getMessage());
            return false;
        }
    }

    // ------------------------------------------------------------------ //
    //  Utility
    // ------------------------------------------------------------------ //

    /**
     * Reset all data (useful for testing).
     */
    public void clear() {
        users.clear();
        courses.clear();
        newsList.clear();
        supportRequests.clear();
        papers.clear();
        journals.clear();
        messages.clear();
        organizations.clear();
        researchProjects.clear();
    }

    /**
     * Print a summary of all stored objects (diagnostic tool).
     */
    public void printSummary() {
        System.out.println("=== DataStore Summary ===");
        System.out.println("  Users   : " + users.size());
        System.out.println("  Courses : " + courses.size());
        System.out.println("  News    : " + newsList.size());
        System.out.println("  Requests: " + supportRequests.size());
        System.out.println("  Papers  : " + papers.size());
        System.out.println("  Journals: " + journals.size());
        System.out.println("  Messages: " + messages.size());
        System.out.println("  Orgs    : " + organizations.size());
        System.out.println("  Projects: " + researchProjects.size());
        System.out.println("=========================");
    }
}
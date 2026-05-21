package storage;

import model.academic.Course;
import model.communication.Message;
import model.communication.News;
import model.research.Journal;
import model.research.ResearchPaper;
import model.support.SupportRequest;
import model.users.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton in-memory data store for the entire UniversitySystem.
 * Acts as the system's "database" — all controllers read and write through this class.
 *
 * Pattern: Singleton (thread-safe via synchronized getInstance()).
 */
public class DataStore {

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
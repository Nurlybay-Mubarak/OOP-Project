package storage;

import model.users.User;
import model.academic.Course;
import model.communication.News;
import model.support.SupportRequest;
import model.research.ResearchPaper;

import java.io.*;
import java.util.*;

/**
 * Singleton class for storing all system data
 */
public class DataStore implements Serializable {

    private static DataStore instance;

    private List<User> users;
    private List<Course> courses;
    private List<News> news;
    private List<SupportRequest> requests;
    private List<ResearchPaper> papers;

    private static final String FILE_NAME = "data.ser";

    /**
     * Private constructor (Singleton)
     */
    private DataStore() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
        news = new ArrayList<>();
        requests = new ArrayList<>();
        papers = new ArrayList<>();
    }

    /**
     * Get singleton instance
     */
    public static DataStore getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    /**
     * Save data to file
     */
    public void save() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load data from file
     */
    private static DataStore load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (DataStore) ois.readObject();
        } catch (Exception e) {
            return new DataStore(); // если файла нет
        }
    }

    // GETTERS

    public List<User> getUsers() {
        return users;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<News> getNews() {
        return news;
    }

    public List<SupportRequest> getRequests() {
        return requests;
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    // ADD METHODS

    public void addUser(User user) {
        users.add(user);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addNews(News n) {
        news.add(n);
    }

    public void addRequest(SupportRequest r) {
        requests.add(r);
    }

    public void addPaper(ResearchPaper p) {
        papers.add(p);
    }
}
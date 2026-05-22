package model.users;

import enums.Language;
import enums.School;
import model.communication.Message;
import model.communication.News;
import patterns.Observer;
import model.research.ResearchPaper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Abstract base class for every user in the UniversitySystem.
 * Implements Observer so any User can subscribe to a Journal and receive
 * notifications about newly published papers.
 */
public abstract class User implements Observer, Serializable {

    private static final long serialVersionUID = 1L;

    private String   id;
    private String   login;
    private String   password;
    private String   firstName;
    private String   lastName;
    private String   email;
    private Language language;
    private School   school;

    /** Inbox: papers that were published after this user subscribed to a journal. */
    private List<ResearchPaper> notifications;

    /** News items this user has authored or can see. */
    private List<News> newsFeed;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public User() {
        this.id            = UUID.randomUUID().toString();
        this.language      = Language.EN;
        this.school        = School.GENERAL;
        this.notifications = new ArrayList<>();
        this.newsFeed      = new ArrayList<>();
    }

    public User(String login, String password, String firstName,
                String lastName, String email) {
        this();
        this.login     = login;
        this.password  = password;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public String getId()               { return id; }
    public void   setId(String id)      { this.id = id; }

    public String getLogin()               { return login; }
    public void   setLogin(String login)   { this.login = login; }

    public String getPassword()                  { return password; }
    public void   setPassword(String password)   { this.password = password; }

    public String getFirstName()                   { return firstName; }
    public void   setFirstName(String firstName)   { this.firstName = firstName; }

    public String getLastName()                  { return lastName; }
    public void   setLastName(String lastName)   { this.lastName = lastName; }

    public String getEmail()             { return email; }
    public void   setEmail(String email) { this.email = email; }

    public Language getLanguage()              { return language; }
    public void     setLanguage(Language lang) { this.language = lang; }

    public School getSchool()              { return school; }
    public void   setSchool(School school) { this.school = school; }

    public List<ResearchPaper> getNotifications() { return notifications; }

    public List<News> getNewsFeed() { return newsFeed; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Validates credentials against stored login/password.
     *
     * @param inputLogin    the login provided
     * @param inputPassword the password provided
     * @return true if both match
     */
    public boolean login(String inputLogin, String inputPassword) {
        return this.login != null
                && this.login.equals(inputLogin)
                && this.password != null
                && this.password.equals(inputPassword);
    }

    /**
     * "Log out" the user (session management is handled by the controller;
     * this method clears any ephemeral session state on the user object).
     */
    public void logout() {
        // Session token clearing would happen here in a real system.
        // In-memory simulation: no persistent session state to clear.
    }

    /**
     * Switch the UI language for this user.
     *
     * @param lang the desired Language
     */
    public void switchLanguage(Language lang) {
        if (lang != null) {
            this.language = lang;
        }
    }

    /**
     * Check whether the provided password matches this user's stored password.
     *
     * @param inputPassword the password to verify
     * @return true if it matches
     */
    public boolean checkPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }

    // ------------------------------------------------------------------ //
    //  Observer Pattern
    // ------------------------------------------------------------------ //

    /**
     * Called by a Journal when a new paper is published.
     * Stores the paper in the user's notification inbox.
     *
     * @param p the newly published paper
     */
    @Override
    public void update(ResearchPaper p) {
        if (p != null && !notifications.contains(p)) {
            notifications.add(p);
            System.out.println("[NOTIFICATION] " + getLogin()
                    + " received update: new paper published -> " + p.getTitle());
        }
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{login='" + login
                + "', name='" + firstName + " " + lastName + "'}";
    }
}
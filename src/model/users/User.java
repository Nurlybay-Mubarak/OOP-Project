package model.users;                    

import patterns.Observer;               
import enums.Language;                  
import model.research.ResearchPaper;    

import java.io.*;
import java.util.*;

/**
 * Abstract base class for all users in the university system.
 * Implements Observer to receive notifications from Journal.
 * Implements Serializable for data persistence.
 */
public abstract class User implements Observer, Serializable {

    private String id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Language language;

    /**
     * Default constructor
     */
    public User() {
        this.language = Language.EN;
    }

    /**
     * Constructor with parameters
     */
    public User(String id, String login, String password,
                String firstName, String lastName, String email) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.language = Language.EN;     // язык по умолчанию
    }

    /**
     * Authenticate user by checking login and password
     */
    public boolean login(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    /**
     * Logout user
     */
    public void logout() {
        System.out.println(firstName + " logged out.");
        
    }

    /**
     * Switch interface language
     */
    public void switchLanguage(Language lang) {
        this.language = lang;
        
    }

    /**
     * Observer pattern — called when new paper is published in subscribed journal
     */
    public void update(String message) {
        System.out.println("Notification for " + firstName + ": " + message);
        
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + login + ")";
    }

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

    // GETTERS & SETTERS 

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }
}
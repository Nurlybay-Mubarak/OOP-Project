package users;

import UniversitySystem.patterns.Observer;
import UniversitySystem.enums.Language;

import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class User implements Observer {

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * 
     */
    private String id;

    /**
     * 
     */
    private String login;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String firstName;

    /**
     * 
     */
    private String lastName;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private Language language;






    /**
     * @return
     */
    public boolean login() {
        // TODO implement here
        return false;
    }

    /**
     * @return
     */
    public void logout() {
        // TODO implement here
        return null;
    }

    /**
     * @param lang 
     * @return
     */
    public void switchLanguage(Language lang) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public String toString() {
        // TODO implement here
        return "";
    }

    /**
     * @param o 
     * @return
     */
    public boolean equals(Object o) {
        // TODO implement here
        return false;
    }

    /**
     * @return
     */
    public int hashCode() {
        // TODO implement here
        return 0;
    }

    /**
     * @param p 
     * @return
     */
    public void update(ResearchPaper p) {
        // TODO implement Observer.update() here
        return null;
    }

}
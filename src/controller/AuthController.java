package controller;                               

import model.users.User;                          
import enums.Language;                       
import storage.DataStore;

import java.io.*;
import java.util.*;

/**
 * Handles authentication: login, logout, language switching.
 * Any user should access the system via authentication (requirement).
 */
public class AuthController {

    /**
     * Default constructor
     */
    public AuthController() {
    }

    /**
     * Login user by checking credentials against all users in DataStore.
     * Returns the User object if found, null if credentials are wrong.
     */
    public User login(String login, String password) {
        List<User> users = DataStore.getInstance().getUsers();

        for (User u : users) {
            if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
                System.out.println("Welcome, " + u.getFirstName() + "!");
                return u;
            }
        }

        System.out.println("Invalid login or password.");
        return null;
    }

    /**
     * Logout current user
     */
    public void logout(User user) {
        if (user != null) {
            user.logout();
            System.out.println(user.getFirstName() + " has been logged out.");
        }
        
    }

    /**
     * Change language for a user (KZ, EN, RU)
     */
    public void changeLanguage(User user, Language language) {
        user.switchLanguage(language);
        System.out.println("Language changed to " + language + " for " + user.getFirstName());
    }
}
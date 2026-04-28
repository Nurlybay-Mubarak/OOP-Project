package controller;                              

import model.users.User;                          
import storage.DataStore;                         

import java.io.*;
import java.util.*;

/**
 * Controller for managing users: add, remove, update, find.
 * Used by Admin to manage the system.
 */
public class UserController {

    private DataStore dataStore;

    /**
     * Default constructor
     */
    public UserController() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Add a new user to the system
     */
    public void addUser(User user) {
        dataStore.addUser(user);
        System.out.println("User added: " + user.getFirstName() + " " + user.getLastName());
    }

    /**
     * Remove a user from the system
     */
    public void removeUser(User user) {
        dataStore.removeUser(user);
        System.out.println("User removed: " + user.getFirstName() + " " + user.getLastName());
    }

    /**
     * Update user info (replaces old user with updated one)
     */
    public void updateUser(User user) {
        List<User> users = dataStore.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                System.out.println("User updated: " + user.getFirstName());
                return;
            }
        }
        System.out.println("User not found: " + user.getId());
    }

    /**
     * Find a user by login
     */
    public User findUserByLogin(String login) {
        for (User u : dataStore.getUsers()) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        System.out.println("User not found with login: " + login);
        return null;
    }

    /**
     * Get all users in the system
     */
    public List<User> getAllUsers() {
        return dataStore.getUsers();
    }
}
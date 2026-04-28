package model.users;                       

import storage.DataStore;

import java.io.*;
import java.util.*;

/**
 * Admin can manage users (add/remove/update) and view log files.
 */
public class Admin extends Employee {

    private List<String> actionLogs;

    /**
     * Default constructor
     */
    public Admin() {
        super();
        this.actionLogs = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     */
    public Admin(String id, String login, String password,
                 String firstName, String lastName, String email,
                 String employeeId, double salary) {
        super(id, login, password, firstName, lastName, email, employeeId, salary);
        this.actionLogs = new ArrayList<>();
    }

    /**
     * Add a new user to the system
     */
    public void addUser(User u) {
        DataStore.getInstance().addUser(u);
        String log = new Date() + " | ADDED user: " + u.getLogin();
        actionLogs.add(log);
        System.out.println("User added: " + u.getFirstName() + " " + u.getLastName());
        
    }

    /**
     * Remove a user from the system
     */
    public void removeUser(User u) {
        DataStore.getInstance().removeUser(u);
        String log = new Date() + " | REMOVED user: " + u.getLogin();
        actionLogs.add(log);
        System.out.println("User removed: " + u.getFirstName() + " " + u.getLastName());
        
    }

    /**
     * Update an existing user
     */
    public void updateUser(User u) {
        String log = new Date() + " | UPDATED user: " + u.getLogin();
        actionLogs.add(log);
        System.out.println("User updated: " + u.getFirstName() + " " + u.getLastName());
    }

    /**
     * View all action logs
     */
    public List<String> viewActionLogs() {
        for (String log : actionLogs) {
            System.out.println(log);
        }
        return actionLogs;
    }

    @Override
    public String toString() {
        return "Admin: " + getFirstName() + " " + getLastName();
    }
}
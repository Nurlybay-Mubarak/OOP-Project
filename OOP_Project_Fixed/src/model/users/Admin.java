package model.users;

import storage.DataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a system Administrator.
 * Admins can add, update, and remove any User, and view the system action logs.
 */
public class Admin extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> actionLogs;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Admin() {
        super();
        this.actionLogs = new ArrayList<>();
    }

    public Admin(String login, String password, String firstName,
                 String lastName, String email, double salary) {
        super(login, password, firstName, lastName, email, salary);
        this.actionLogs = new ArrayList<>();
    }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Add a new user to the DataStore and record the action.
     *
     * @param u the user to add
     */
    public void addUser(User u) {
        if (u == null) return;
        DataStore.getInstance().addUser(u);
        String log = "[ADMIN] " + getLogin() + " added user: " + u.getLogin();
        actionLogs.add(log);
        System.out.println(log);
    }

    /**
     * Remove a user from the DataStore and record the action.
     *
     * @param u the user to remove
     */
    public void removeUser(User u) {
        if (u == null) return;
        DataStore.getInstance().removeUser(u);
        String log = "[ADMIN] " + getLogin() + " removed user: " + u.getLogin();
        actionLogs.add(log);
        System.out.println(log);
    }

    /**
     * Update a user's record in the DataStore and record the action.
     * (In-memory: the object reference is already updated; this records the event.)
     *
     * @param u the user that has been updated
     */
    public void updateUser(User u) {
        if (u == null) return;
        DataStore.getInstance().updateUser(u);
        String log = "[ADMIN] " + getLogin() + " updated user: " + u.getLogin();
        actionLogs.add(log);
        System.out.println(log);
    }

    /**
     * Return the full list of admin action log entries.
     *
     * @return unmodifiable list of log strings
     */
    public List<String> viewActionLogs() {
        return java.util.Collections.unmodifiableList(actionLogs);
    }

    /**
     * Manually record a custom action log entry.
     *
     * @param entry the log message to record
     */
    public void logAction(String entry) {
        if (entry != null && !entry.isBlank()) {
            String log = "[ADMIN] " + getLogin() + " | " + entry;
            actionLogs.add(log);
        }
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() { return super.hashCode(); }

    @Override
    public String toString() {
        return "Admin{login='" + getLogin() + "', name='" + getFirstName()
                + " " + getLastName() + "', logs=" + actionLogs.size() + "}";
    }
}
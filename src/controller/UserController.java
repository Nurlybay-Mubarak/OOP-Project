package controller;

import model.users.User;
import patterns.UserFactory;
import storage.DataStore;

import java.util.List;
import java.util.Optional;

/**
 * Handles CRUD operations for User objects via the DataStore.
 */
public class UserController {

    public UserController() {}

    /**
     * Add a user to the DataStore.
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        if (user == null) return;
        DataStore.getInstance().addUser(user);
        System.out.println("[USER] Added: " + user.getLogin());
    }

    /**
     * Remove a user from the DataStore.
     *
     * @param user the user to remove
     */
    public void removeUser(User user) {
        if (user == null) return;
        DataStore.getInstance().removeUser(user);
        System.out.println("[USER] Removed: " + user.getLogin());
    }

    /**
     * Update a user's record (in-memory; the object is already mutated).
     *
     * @param user the user that has been updated
     */
    public void updateUser(User user) {
        if (user == null) return;
        DataStore.getInstance().updateUser(user);
        System.out.println("[USER] Updated: " + user.getLogin());
    }

    /**
     * Find a user by their login string.
     *
     * @param login the login to search for
     * @return the User, or null if not found
     */
    public User findUserByLogin(String login) {
        if (login == null) return null;
        Optional<User> found = DataStore.getInstance().findUserByLogin(login);
        return found.orElse(null);
    }

    /**
     * Create and immediately register a new user of the given type.
     *
     * @param type      user type string (STUDENT, TEACHER, etc.)
     * @param login     login name
     * @param password  password
     * @param firstName first name
     * @param lastName  last name
     * @param email     email
     * @return the newly created and registered User
     */
    public User createAndRegister(String type, String login, String password,
                                   String firstName, String lastName, String email) {
        User u = UserFactory.createUser(type, login, password, firstName, lastName, email);
        if (u != null) {
            addUser(u);
        }
        return u;
    }

    /**
     * Return all users currently in the system.
     */
    public List<User> getAllUsers() {
        return DataStore.getInstance().getAllUsers();
    }
}
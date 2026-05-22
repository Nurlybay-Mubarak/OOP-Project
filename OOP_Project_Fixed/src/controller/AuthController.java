package controller;

import enums.Language;
import model.users.User;
import storage.DataStore;

import java.util.Optional;

/**
 * Handles authentication: login, logout, and language switching.
 */
public class AuthController {

    private static User currentUser = null;

    public AuthController() {}

    /**
     * Authenticate a user by login and password.
     * Returns the User object on success, null on failure.
     *
     * @param login    the login string
     * @param password the password string
     * @return matched User, or null if credentials are invalid
     */
    public User login(String login, String password) {
        if (login == null || password == null) return null;
        Optional<User> found = DataStore.getInstance().findUserByLoginAndPassword(login, password);
        if (found.isPresent()) {
            currentUser = found.get();
            System.out.println("[AUTH] Login successful: " + currentUser.getLogin()
                    + " (" + currentUser.getClass().getSimpleName() + ")");
            return currentUser;
        }
        System.out.println("[AUTH] Login failed for: " + login);
        return null;
    }

    /**
     * Log out the currently active user.
     *
     * @param user the user to log out
     */
    public void logout(User user) {
        if (user != null) {
            user.logout();
            System.out.println("[AUTH] " + user.getLogin() + " logged out.");
        }
        currentUser = null;
    }

    /**
     * Change the interface language for a user.
     *
     * @param user     the user whose language preference is being changed
     * @param language the new language
     */
    public void changeLanguage(User user, Language language) {
        if (user == null || language == null) return;
        user.switchLanguage(language);
        System.out.println("[AUTH] " + user.getLogin()
                + " switched language to " + language);
    }

    /** Returns the user currently logged in (null if no active session). */
    public static User getCurrentUser() {
        return currentUser;
    }
}
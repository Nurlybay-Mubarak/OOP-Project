package boundary;                                 

import controller.AuthController;                 
import model.users.User;                          

import java.io.*;
import java.util.*;

/**
 * Console UI for login/logout.
 * First screen the user sees when starting the system.
 */
public class LoginBoundary {

    private Scanner scanner;
    private AuthController authController;

    /**
     * Default constructor
     */
    public LoginBoundary() {
        this.scanner = new Scanner(System.in);
        this.authController = new AuthController();
    }

    /**
     * Show login menu and handle authentication
     */
    public User showLoginMenu() {
        System.out.println("========================================");
        System.out.println("  University Management System - Login  ");
        System.out.println("========================================");

        String login = inputLogin();
        String password = inputPassword();

        User user = authController.login(login, password);

        if (user == null) {
            showError("Invalid credentials. Try again.");
        }

        return user;
    }

    /**
     * Prompt user for login
     */
    public String inputLogin() {
        System.out.print("Login: ");
        return scanner.nextLine().trim();
    }

    /**
     * Prompt user for password
     */
    public String inputPassword() {
        System.out.print("Password: ");
        return scanner.nextLine().trim();
    }

    /**
     * Show error message
     */
    public void showError(String message) {
        System.out.println("[ERROR] " + message);
    }

    /**
     * Show success message
     */
    public void showSuccess(String message) {
        System.out.println("[OK] " + message);
    }
}
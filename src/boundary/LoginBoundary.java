package boundary;

import controller.AuthController;
import model.users.User;

import java.util.Scanner;

/**
 * Boundary class for the login screen.
 * Handles user input for credentials and delegates to AuthController.
 */
public class LoginBoundary {

    private final AuthController authController;
    private final Scanner        scanner;

    public LoginBoundary() {
        this.authController = new AuthController();
        this.scanner        = new Scanner(System.in);
    }

    /**
     * Display the login prompt and return the authenticated User.
     * Retries up to 3 times before returning null.
     *
     * @return authenticated User, or null after 3 failed attempts
     */
    public User showLoginMenu() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   University Information System  ║");
        System.out.println("║            LOGIN                 ║");
        System.out.println("╚══════════════════════════════════╝");

        int attempts = 0;
        while (attempts < 3) {
            String login    = inputLogin();
            String password = inputPassword();
            User user = authController.login(login, password);
            if (user != null) return user;
            showError("Invalid credentials. Attempts left: " + (2 - attempts));
            attempts++;
        }
        showError("Too many failed attempts. Exiting.");
        return null;
    }

    public String inputLogin() {
        System.out.print("  Login    : ");
        return scanner.nextLine().trim();
    }

    public String inputPassword() {
        System.out.print("  Password : ");
        return scanner.nextLine().trim();
    }

    public void showError(String message) {
        System.out.println("  [ERROR] " + message);
    }

    public AuthController getAuthController() { return authController; }
}
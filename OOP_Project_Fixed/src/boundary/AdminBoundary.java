package boundary;

import model.users.Admin;
import model.users.User;
import patterns.UserFactory;
import storage.DataStore;

import java.util.Scanner;

/**
 * Console UI for Admin role — full feature coverage including updateUser.
 */
public class AdminBoundary {

    private final Admin   admin;
    private final Scanner scanner;

    public AdminBoundary(Admin admin) {
        this.admin   = admin;
        this.scanner = new Scanner(System.in);
    }

    public void showAdminMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══ ADMIN MENU ═══════════════════════╗");
            System.out.println("║  1.  Add user                       ║");
            System.out.println("║  2.  Remove user                    ║");
            System.out.println("║  3.  Update user password           ║");
            System.out.println("║  4.  View all users                 ║");
            System.out.println("║  5.  View action logs               ║");
            System.out.println("║  6.  System summary                 ║");
            System.out.println("║  7.  View all courses               ║");
            System.out.println("║  8.  View all support requests      ║");
            System.out.println("║  9.  Change language                ║");
            System.out.println("║  0.  Logout                         ║");
            System.out.println("╚═════════════════════════════════════╝");
            System.out.print("  Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": addUser();              break;
                case "2": removeUser();           break;
                case "3": updateUserPassword();   break;
                case "4": viewAllUsers();         break;
                case "5": viewLogs();             break;
                case "6": systemSummary();        break;
                case "7": viewAllCourses();       break;
                case "8": viewAllRequests();      break;
                case "9": changeLanguage();       break;
                case "0": running = false;        break;
                default:  System.out.println("  Invalid option.");
            }
        }
    }

    // ── 1. Add User ──────────────────────────────────────────────────────
    public void addUser() {
        System.out.println("  Type: STUDENT / GRADUATE / TEACHER / MANAGER / SUPPORT / ADMIN");
        System.out.print("  Type     : "); String type  = scanner.nextLine().trim().toUpperCase();
        System.out.print("  Login    : "); String login = scanner.nextLine().trim();
        System.out.print("  Password : "); String pass  = scanner.nextLine().trim();
        System.out.print("  FirstName: "); String fn    = scanner.nextLine().trim();
        System.out.print("  LastName : "); String ln    = scanner.nextLine().trim();
        System.out.print("  Email    : "); String email = scanner.nextLine().trim();

        // Check duplicate
        if (DataStore.getInstance().findUserByLogin(login).isPresent()) {
            System.out.println("  [ERROR] Login already exists: " + login);
            return;
        }
        User u = UserFactory.createUser(type, login, pass, fn, ln, email);
        if (u != null) {
            admin.addUser(u);
            System.out.println("  User added: " + u.getLogin() + " [" + u.getClass().getSimpleName() + "]");
        } else {
            System.out.println("  Unknown type: " + type);
        }
    }

    // ── 2. Remove User ───────────────────────────────────────────────────
    public void removeUser() {
        System.out.print("  Login to remove: ");
        String login = scanner.nextLine().trim();
        DataStore.getInstance().findUserByLogin(login).ifPresentOrElse(
            u -> {
                System.out.print("  Confirm removal of [" + u.getLogin() + "]? (y/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                    admin.removeUser(u);
                    System.out.println("  Removed: " + login);
                } else {
                    System.out.println("  Cancelled.");
                }
            },
            () -> System.out.println("  User not found: " + login)
        );
    }

    // ── 3. Update User Password ──────────────────────────────────────────
    public void updateUserPassword() {
        System.out.print("  User login    : "); String login   = scanner.nextLine().trim();
        System.out.print("  New password  : "); String newPass = scanner.nextLine().trim();
        DataStore.getInstance().findUserByLogin(login).ifPresentOrElse(
            u -> {
                u.setPassword(newPass);
                admin.logAction("Updated password for user: " + login);
                System.out.println("  Password updated for: " + login);
            },
            () -> System.out.println("  User not found.")
        );
    }

    // ── 4. View All Users ────────────────────────────────────────────────
    public void viewAllUsers() {
        var users = DataStore.getInstance().getAllUsers();
        if (users.isEmpty()) { System.out.println("  No users."); return; }
        System.out.println("\n  --- All Users (" + users.size() + ") ---");
        users.forEach(u -> System.out.printf("  %-15s | %-20s | %s%n",
                u.getLogin(),
                u.getLastName() + " " + u.getFirstName(),
                u.getClass().getSimpleName()));
    }

    // ── 5. View Action Logs ──────────────────────────────────────────────
    public void viewLogs() {
        var logs = admin.viewActionLogs();
        if (logs.isEmpty()) { System.out.println("  No logs yet."); return; }
        System.out.println("\n  --- Action Logs ---");
        logs.forEach(l -> System.out.println("  " + l));
    }

    // ── 6. System Summary ────────────────────────────────────────────────
    public void systemSummary() {
        DataStore.getInstance().printSummary();
    }

    // ── 7. View All Courses ──────────────────────────────────────────────
    public void viewAllCourses() {
        var courses = DataStore.getInstance().getAllCourses();
        if (courses.isEmpty()) { System.out.println("  No courses."); return; }
        System.out.println("\n  --- All Courses ---");
        courses.forEach(c -> System.out.println("  " + c.getInfo()));
    }

    // ── 8. View All Support Requests ────────────────────────────────────
    public void viewAllRequests() {
        var requests = DataStore.getInstance().getAllSupportRequests();
        if (requests.isEmpty()) { System.out.println("  No requests."); return; }
        System.out.println("\n  --- All Support Requests ---");
        requests.forEach(r -> System.out.printf("  [%s] %-8s | %-8s | %s%n",
                r.getId().substring(0,6), r.getStatus(), r.getUrgencyLevel(), r.getDescription()));
    }

    // ── 9. Change Language ───────────────────────────────────────────────
    public void changeLanguage() {
        System.out.println("  1. KZ   2. RU   3. EN");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        enums.Language lang = ch.equals("1") ? enums.Language.KZ : ch.equals("2") ? enums.Language.RU : enums.Language.EN;
        admin.setLanguage(lang);
        System.out.println("  Language set to: " + lang);
    }
}
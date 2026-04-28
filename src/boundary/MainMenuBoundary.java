package boundary;                                          // ДОБАВЛЕНО

import model.users.*;                                      // ИСПРАВЛЕНО: убран "UniversitySystem."
import model.research.Researcher;                          // ИСПРАВЛЕНО

import java.io.*;
import java.util.*;

/**
 * Main menu that redirects user to their specific menu
 * based on their role (Student, Teacher, Admin, etc.)
 */
public class MainMenuBoundary {

    private Scanner scanner;

    /**
     * Default constructor
     */
    public MainMenuBoundary() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Detect user type and open corresponding menu
     */
    public void showMainMenu(User user) {
        System.out.println("\n========================================");
        System.out.println("  Welcome, " + user.getFirstName() + "!");
        System.out.println("========================================");

        if (user instanceof Admin) {
            openAdminMenu((Admin) user);
        } else if (user instanceof Manager) {
            openManagerMenu((Manager) user);
        } else if (user instanceof TechSupportSpecialist) {
            openTechSupportMenu((TechSupportSpecialist) user);
        } else if (user instanceof Teacher) {
            openTeacherMenu((Teacher) user);
        } else if (user instanceof Student) {
            openStudentMenu((Student) user);
        } else {
            System.out.println("Unknown user role.");
        }
    }

    /**
     * Student menu
     */
    public void openStudentMenu(Student student) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View Courses");
            System.out.println("2. Register for Course");
            System.out.println("3. View Marks");
            System.out.println("4. View Transcript");
            System.out.println("5. Rate Teacher");
            System.out.println("6. Join Organization");
            System.out.println("7. Switch Language");
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: student.viewCourses(); break;
                case 2: System.out.println("TODO: course registration"); break;
                case 3: student.viewMarks(); break;
                case 4: student.getTranscript(); break;
                case 5: System.out.println("TODO: rate teacher"); break;
                case 6: System.out.println("TODO: join org"); break;
                case 7: System.out.println("TODO: switch lang"); break;
                case 0: running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Teacher menu
     */
    public void openTeacherMenu(Teacher teacher) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Teacher Menu ---");
            System.out.println("1. View Courses");
            System.out.println("2. Manage Course");
            System.out.println("3. Put Marks");
            System.out.println("4. View Students");
            System.out.println("5. Send Complaint");
            System.out.println("6. Send Message");
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: teacher.viewCourses(); break;
                case 2: System.out.println("TODO: manage course"); break;
                case 3: System.out.println("TODO: put marks"); break;
                case 4: teacher.viewStudents(); break;
                case 5: System.out.println("TODO: send complaint"); break;
                case 6: System.out.println("TODO: send message"); break;
                case 0: running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Manager menu
     */
    public void openManagerMenu(Manager manager) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. Assign Course to Teacher");
            System.out.println("2. Approve Registration");
            System.out.println("3. Create Report");
            System.out.println("4. Manage News");
            System.out.println("5. View Students (sorted)");
            System.out.println("6. View Teachers");
            System.out.println("7. View Employee Requests");
            System.out.println("8. Send Message");
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: System.out.println("TODO: assign course"); break;
                case 2: System.out.println("TODO: approve registration"); break;
                case 3: manager.createReport(); break;
                case 4: System.out.println("TODO: manage news"); break;
                case 5: System.out.println("TODO: view students sorted"); break;
                case 6: manager.viewTeachersInfo(); break;
                case 7: manager.viewEmployeeRequests(); break;
                case 8: System.out.println("TODO: send message"); break;
                case 0: running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Admin menu
     */
    public void openAdminMenu(Admin admin) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add User");
            System.out.println("2. Remove User");
            System.out.println("3. Update User");
            System.out.println("4. View Action Logs");
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: System.out.println("TODO: add user"); break;
                case 2: System.out.println("TODO: remove user"); break;
                case 3: System.out.println("TODO: update user"); break;
                case 4: admin.viewActionLogs(); break;
                case 0: running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Researcher menu (teacher or gradstudent who is researcher)
     */
    public void openResearchMenu(Researcher researcher) {
        System.out.println("\n--- Researcher Menu ---");
        System.out.println("1. Publish Paper");
        System.out.println("2. Calculate H-Index");
        System.out.println("3. Print Papers");
        System.out.println("4. Join Project");
        System.out.println("0. Back");
        // TODO: Человек 3 доделает эту часть
    }

    /**
     * Tech Support menu
     */
    public void openTechSupportMenu(TechSupportSpecialist specialist) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Tech Support Menu ---");
            System.out.println("1. View New Requests");
            System.out.println("2. Accept Request");
            System.out.println("3. Reject Request");
            System.out.println("4. Mark as Done");
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: specialist.viewNewRequests(); break;
                case 2: System.out.println("TODO: accept request"); break;
                case 3: System.out.println("TODO: reject request"); break;
                case 4: System.out.println("TODO: mark done"); break;
                case 0: running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }
}
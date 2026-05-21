package boundary;

import model.users.*;

/**
 * Main menu router — directs authenticated users to their role-specific boundary.
 */
public class MainMenuBoundary {

    public MainMenuBoundary() {}

    /**
     * Show the appropriate full menu based on the user's runtime type.
     *
     * @param user the authenticated user
     */
    public void showMainMenu(User user) {
        System.out.println("\n  ══════════════════════════════════════");
        System.out.printf ("  Welcome, %s %s [%s]%n",
                user.getFirstName(), user.getLastName(),
                user.getClass().getSimpleName());
        System.out.println("  Language : " + user.getLanguage());
        System.out.println("  ══════════════════════════════════════");

        if      (user instanceof GraduateStudent)   new GraduateStudentBoundary((GraduateStudent) user).showStudentMenu();
        else if (user instanceof Student)            new StudentBoundary((Student) user).showStudentMenu();
        else if (user instanceof Teacher)            new TeacherBoundary((Teacher) user).showTeacherMenu();
        else if (user instanceof Manager)            new ManagerBoundary((Manager) user).showManagerMenu();
        else if (user instanceof Admin)              new AdminBoundary((Admin) user).showAdminMenu();
        else if (user instanceof TechSupportSpecialist) new TechSupportBoundary((TechSupportSpecialist) user).showSupportMenu();
        else System.out.println("  [MENU] No menu defined for: " + user.getClass().getSimpleName());
    }
}
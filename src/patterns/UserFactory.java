package patterns;

import enums.ManagerType;
import enums.StudentType;
import enums.TeacherPosition;
import model.users.*;

/**
 * Factory Method pattern implementation.
 * Creates User objects by type string, without exposing instantiation logic to callers.
 */
public class UserFactory {

    /**
     * Create a User of the specified type with default placeholder credentials.
     * In production use, credentials are set via setters after creation.
     *
     * @param type one of: "STUDENT", "GRADUATE", "TEACHER", "MANAGER", "ADMIN", "SUPPORT"
     * @return a new User instance, or null if the type is unknown
     */
    public static User createUser(String type) {
        if (type == null) return null;
        switch (type.toUpperCase()) {
            case "STUDENT":
                return new Student("student_" + uid(), "pass",
                        "Student", "User", "student@uni.kz", StudentType.BACHELOR);
            case "GRADUATE":
                return new GraduateStudent("grad_" + uid(), "pass",
                        "Graduate", "Student", "grad@uni.kz", StudentType.MASTER);
            case "TEACHER":
                return new Teacher("teacher_" + uid(), "pass",
                        "Teacher", "User", "teacher@uni.kz", 150000, TeacherPosition.LECTOR);
            case "MANAGER":
                return new Manager("manager_" + uid(), "pass",
                        "Manager", "User", "manager@uni.kz", 200000, ManagerType.OR);
            case "ADMIN":
                return new Admin("admin_" + uid(), "pass",
                        "Admin", "User", "admin@uni.kz", 250000);
            case "SUPPORT":
                return new TechSupportSpecialist("support_" + uid(), "pass",
                        "Support", "Specialist", "support@uni.kz", 120000);
            default:
                System.out.println("[UserFactory] Unknown user type: " + type);
                return null;
        }
    }

    /**
     * Create a fully configured User with explicit credentials.
     *
     * @param type      user type string
     * @param login     login name
     * @param password  password
     * @param firstName first name
     * @param lastName  last name
     * @param email     email address
     * @return configured User instance
     */
    public static User createUser(String type, String login, String password,
                                   String firstName, String lastName, String email) {
        if (type == null) return null;
        switch (type.toUpperCase()) {
            case "STUDENT": {
                Student s = new Student(login, password, firstName, lastName, email, StudentType.BACHELOR);
                return s;
            }
            case "GRADUATE": {
                GraduateStudent g = new GraduateStudent(login, password, firstName, lastName, email, StudentType.MASTER);
                return g;
            }
            case "TEACHER": {
                Teacher t = new Teacher(login, password, firstName, lastName, email, 150000, TeacherPosition.LECTOR);
                return t;
            }
            case "MANAGER": {
                Manager m = new Manager(login, password, firstName, lastName, email, 200000, ManagerType.OR);
                return m;
            }
            case "ADMIN": {
                Admin a = new Admin(login, password, firstName, lastName, email, 250000);
                return a;
            }
            case "SUPPORT": {
                TechSupportSpecialist sp = new TechSupportSpecialist(login, password, firstName, lastName, email, 120000);
                return sp;
            }
            default:
                return null;
        }
    }

    private static int counter = 1;
    private static synchronized String uid() {
        return String.valueOf(counter++);
    }
}
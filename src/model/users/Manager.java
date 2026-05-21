package model.users;

import enums.ManagerType;
import model.academic.Course;
import model.communication.News;
import model.support.SupportRequest;
import storage.DataStore;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a university Manager (Office of the Registrar, Department Head, Dean).
 * Responsible for course registration management, news, and reports.
 */
public class Manager extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private ManagerType managerType;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Manager() {
        super();
    }

    public Manager(String login, String password, String firstName,
                   String lastName, String email, double salary,
                   ManagerType managerType) {
        super(login, password, firstName, lastName, email, salary);
        this.managerType = managerType;
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public ManagerType getManagerType()                  { return managerType; }
    public void        setManagerType(ManagerType type)  { this.managerType = type; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Assign a teacher to a course and store the relationship in both objects.
     *
     * @param c the course
     * @param t the teacher to assign
     */
    public void assignCourseToTeacher(Course c, Teacher t) {
        if (c == null || t == null) return;
        c.addTeacher(t);
        t.manageCourse(c);
        System.out.println("[MANAGER] " + getLogin() + " assigned "
                + t.getLogin() + " to course " + c.getCode());
    }

    /**
     * Approve a student's registration for a course.
     * Enrolls the student in the course on the Course side.
     *
     * @param s the student whose registration is approved
     * @param c the course being approved
     */
    public void approveRegistration(Student s, Course c) {
        if (s == null || c == null) return;
        boolean enrolled = c.enrollStudent(s);
        if (enrolled) {
            System.out.println("[MANAGER] " + getLogin()
                    + " approved " + s.getLogin() + " for course " + c.getCode());
        } else {
            System.out.println("[MANAGER] Could not enroll " + s.getLogin()
                    + " in " + c.getCode() + " (full or already enrolled).");
        }
    }

    /**
     * Open a course for registration (marks it available in the DataStore).
     *
     * @param c     the course to open
     * @param year  the academic year
     * @param major the major/program this course is opened for
     */
    public void openCourseForRegistration(Course c, int year, String major) {
        if (c == null) return;
        DataStore.getInstance().addCourse(c);
        System.out.println("[MANAGER] " + getLogin() + " opened course "
                + c.getCode() + " (" + major + ", " + year + ")");
    }

    /**
     * Generate a summary report string for all students.
     *
     * @return formatted report
     */
    public String createReport() {
        List<Student> students = DataStore.getInstance().getAllStudents();
        StringBuilder sb = new StringBuilder();
        sb.append("=== Student Summary Report ===\n");
        sb.append(String.format("  Total students: %d%n", students.size()));
        double avgGpa = students.stream().mapToDouble(Student::getGpa).average().orElse(0.0);
        sb.append(String.format("  Average GPA: %.2f%n", avgGpa));
        return sb.toString();
    }

    /**
     * Create or update a news article (adds/replaces in the DataStore).
     *
     * @param n the news item to manage
     */
    public void manageNews(News n) {
        if (n == null) return;
        n.setAuthor(this);
        DataStore.getInstance().addNews(n);
        System.out.println("[MANAGER] " + getLogin() + " published news: " + n.getTitle());
    }

    /**
     * Return all students sorted by GPA descending (highest GPA first).
     */
    public List<Student> viewStudentsByGpa() {
        return viewStudentsInfo(
            java.util.Comparator.comparingDouble(Student::getGpa).reversed());
    }

    /**
     * Return all students sorted alphabetically by last name.
     */
    public List<Student> viewStudentsAlphabetically() {
        return viewStudentsInfo(
            java.util.Comparator.comparing(Student::getLastName)
                .thenComparing(Student::getFirstName));
    }

    /**
     * View all students sorted by the provided comparator.
     *
     * @param comp a Comparator for Student objects
     * @return sorted list
     */
    public List<Student> viewStudentsInfo(Comparator<Student> comp) {
        List<Student> list = DataStore.getInstance().getAllStudents();
        list.sort(comp);
        return list;
    }

    /**
     * View all teachers registered in the system.
     *
     * @return list of Teacher objects
     */
    public List<Teacher> viewTeachersInfo() {
        return DataStore.getInstance().getAllTeachers();
    }

    /**
     * View all pending employee support requests.
     *
     * @return list of SupportRequest objects with NEW or VIEWED status
     */
    public List<SupportRequest> viewEmployeeRequests() {
        return DataStore.getInstance().getAllSupportRequests().stream()
                .filter(r -> r.getStatus() == enums.RequestStatus.NEW
                          || r.getStatus() == enums.RequestStatus.VIEWED)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Manager manager = (Manager) o;
        return managerType == manager.managerType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), managerType);
    }

    @Override
    public String toString() {
        return "Manager{login='" + getLogin() + "', name='" + getFirstName()
                + " " + getLastName() + "', type=" + managerType + "}";
    }
}
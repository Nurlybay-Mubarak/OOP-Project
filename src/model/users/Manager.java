package model.users;                             

import enums.ManagerType;                        
import model.academic.Course;                     
import model.communication.News;                  
import model.support.SupportRequest;              
import storage.DataStore;                         

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager can assign courses, approve registration, manage news,
 * create reports, and view info about students/teachers.
 * Manager types: OR, DEPARTMENT, DEAN_OFFICE (enumeration).
 */
public class Manager extends Employee {

    private ManagerType managerType;               

    /**
     * Default constructor
     */
    public Manager() {
        super();
    }

    /**
     * Constructor with parameters
     */
    public Manager(String id, String login, String password,
                   String firstName, String lastName, String email,
                   String employeeId, double salary, ManagerType managerType) {
        super(id, login, password, firstName, lastName, email, employeeId, salary);
        this.managerType = managerType;
    }

    /**
     * Assign a course to a teacher
     */
    public void assignCourseToTeacher(Course c, Teacher t) {
        t.addCourse(c);
        c.addTeacher(t);
        System.out.println("Course " + c.getName() + " assigned to " + t.getFirstName());
    }

    /**
     * Approve student's course registration
     */
    public void approveRegistration(Student s) {
        System.out.println("Registration approved for student: "
                + s.getFirstName() + " " + s.getLastName());
    }

    /**
     * Open a course for registration for specific year and major
     */
    public void openCourseForRegistration(Course c, int year, String major) {
        c.setAvailableForRegistration(true);
        c.setTargetYear(year);
        c.setTargetMajor(major);
        System.out.println("Course " + c.getName()
                + " opened for year " + year + ", major: " + major);
    }

    /**
     * Create statistical report on academic performance
     */
    public String createReport() {
        DataStore ds = DataStore.getInstance();
        List<Student> students = ds.getStudents();

        double avgGpa = students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);

        String report = "=== Academic Performance Report ===\n"
                + "Total students: " + students.size() + "\n"
                + "Average GPA: " + String.format("%.2f", avgGpa) + "\n";

        System.out.println(report);
        return report;
    }

    /**
     * Manage news (create, pin research topics)
     */
    public void manageNews(News n) {
        DataStore.getInstance().addNews(n);
        System.out.println("News managed: " + n.getTitle());
    }

    /**
     * View students info sorted by given comparator (GPA, alphabetically, etc.)
     */
    public List<Student> viewStudentsInfo(Comparator<Student> comp) {
        List<Student> students = new ArrayList<>(DataStore.getInstance().getStudents());
        students.sort(comp);
        for (Student s : students) {
            System.out.println(s);
        }
        return students;
    }

    /**
     * View all teachers info
     */
    public List<Teacher> viewTeachersInfo() {
        List<Teacher> teachers = DataStore.getInstance().getTeachers();
        for (Teacher t : teachers) {
            System.out.println(t);
        }
        return teachers;
    }

    /**
     * View requests from employees (signed by dean/rector)
     */
    public List<SupportRequest> viewEmployeeRequests() {
        List<SupportRequest> requests = DataStore.getInstance().getRequests();
        for (SupportRequest r : requests) {
            System.out.println(r);
        }
        return requests;
    }

    // GETTERS & SETTERS

    public ManagerType getManagerType() { return managerType; }
    public void setManagerType(ManagerType managerType) { this.managerType = managerType; }

    @Override
    public String toString() {
        return "Manager: " + getFirstName() + " " + getLastName()
                + " (" + managerType + ")";
    }
}
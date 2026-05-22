package controller;

import model.academic.Course;
import model.users.Student;
import model.users.Teacher;
import storage.DataStore;

import java.util.Comparator;
import java.util.List;

/**
 * Handles generation of system reports for managers and teachers.
 */
public class ReportController {

    public ReportController() {}

    /**
     * Generate a marks report for all enrolled students in a course.
     *
     * @param course the course to report on
     * @return formatted report string
     */
    public String generateMarksReport(Course course) {
        if (course == null) return "No course specified.";
        StringBuilder sb = new StringBuilder();
        sb.append("=== Marks Report: ").append(course.getName())
          .append(" [").append(course.getCode()).append("] ===\n");
        sb.append(String.format("  Enrolled: %d / %d%n",
                course.getEnrolledStudents().size(), course.getMaxStudents()));
        sb.append("  ---\n");
        for (Student s : course.getEnrolledStudents()) {
            s.viewMarks().stream()
             .filter(m -> course.equals(m.getCourse()))
             .forEach(m -> sb.append(String.format("  %-25s %s  %.1f%n",
                     s.getLastName() + ", " + s.getFirstName(),
                     m.getLetterGrade(), m.getTotal())));
        }
        return sb.toString();
    }

    /**
     * Generate a summary report for a single student.
     *
     * @param student the student to report on
     * @return formatted report string
     */
    public String generateStudentReport(Student student) {
        if (student == null) return "No student specified.";
        StringBuilder sb = new StringBuilder();
        sb.append("=== Student Report ===\n");
        sb.append("  Login   : ").append(student.getLogin()).append("\n");
        sb.append("  Name    : ").append(student.getFirstName())
          .append(" ").append(student.getLastName()).append("\n");
        sb.append("  Type    : ").append(student.getStudentType()).append("\n");
        sb.append("  GPA     : ").append(String.format("%.2f", student.getGpa())).append("\n");
        sb.append("  Credits : ").append(student.getCredits()).append(" / ")
          .append(Student.MAX_CREDITS).append("\n");
        sb.append("  Fails   : ").append(student.getFailedCount()).append("\n");
        sb.append("  Courses : ").append(student.getCourses().size()).append("\n");
        return sb.toString();
    }

    /**
     * Return all students sorted by GPA descending (leaderboard).
     *
     * @return sorted list of students
     */
    public List<Student> generateGpaRanking() {
        List<Student> students = DataStore.getInstance().getAllStudents();
        students.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        return students;
    }

    /**
     * Generate a department-wide teacher listing with their ratings.
     *
     * @return formatted report string
     */
    public String generateTeacherReport() {
        List<Teacher> teachers = DataStore.getInstance().getAllTeachers();
        StringBuilder sb = new StringBuilder();
        sb.append("=== Teacher Report ===\n");
        for (Teacher t : teachers) {
            sb.append(String.format("  %-20s | %-15s | Rating: %.2f (%d reviews)%n",
                    t.getLastName() + " " + t.getFirstName(),
                    t.getPosition(),
                    t.getRating(),
                    t.getRatingCount()));
        }
        return sb.toString();
    }
}
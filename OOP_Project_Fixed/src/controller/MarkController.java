package controller;

import model.academic.Course;
import model.academic.Mark;
import model.users.Student;
import model.users.Teacher;

import java.util.List;

/**
 * Handles mark assignment and GPA computation.
 */
public class MarkController {

    public MarkController() {}

    /**
     * Put a mark for a student in a course.
     * Validates that the teacher teaches the course and that the student is enrolled.
     *
     * @param teacher the teacher assigning the mark
     * @param student the student receiving the mark
     * @param course  the course the mark belongs to
     * @param mark    the Mark object to assign
     */
    public void putMark(Teacher teacher, Student student, Course course, Mark mark) {
        if (teacher == null || student == null || course == null || mark == null) return;

        if (!teacher.getCourses().contains(course)) {
            System.out.println("[MARK] " + teacher.getLogin()
                    + " is not assigned to course " + course.getCode());
            return;
        }
        if (!course.getEnrolledStudents().contains(student)) {
            System.out.println("[MARK] " + student.getLogin()
                    + " is not enrolled in " + course.getCode());
            return;
        }

        // Check if another teacher already assigned a mark for this course
        var existingMark = student.getTranscript().getMarks().stream()
                .filter(m -> course.equals(m.getCourse()))
                .findFirst()
                .orElse(null);
        if (existingMark != null && existingMark.getAssignedBy() != null
                && !existingMark.getAssignedBy().equals(teacher)) {
            System.out.println("[MARK] Cannot overwrite mark for " + student.getLogin()
                    + " in " + course.getCode()
                    + " — already assigned by " + existingMark.getAssignedBy().getLogin());
            return;
        }

        mark.setAssignedBy(teacher);
        teacher.putMark(student, course, mark);
        System.out.println("[MARK] Mark recorded for " + student.getLogin()
                + " in " + course.getCode()
                + ": " + mark.getLetterGrade() + " (" + mark.getTotal() + ")");
    }

    /**
     * Return all marks from a student's transcript.
     *
     * @param student the student to query
     * @return list of marks
     */
    public List<Mark> getMarks(Student student) {
        if (student == null) return java.util.Collections.emptyList();
        return student.getTranscript().getMarks();
    }

    /**
     * Calculate and return the current GPA for a student.
     *
     * @param student the student
     * @return GPA value on a 4.0 scale
     */
    public double calculateGpa(Student student) {
        if (student == null) return 0.0;
        return student.getTranscript().calculateGpa();
    }
}
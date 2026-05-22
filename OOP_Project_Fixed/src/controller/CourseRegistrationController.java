package controller;

import exceptions.CreditLimitException;
import model.academic.Course;
import model.users.Manager;
import model.users.Student;
import storage.DataStore;

/**
 * Handles all course registration business logic.
 * Validates credit limits and delegates enrollment to model objects.
 */
public class CourseRegistrationController {

    public CourseRegistrationController() {}

    /**
     * Register a student for a course.
     * Delegates to Student.registerCourse() which enforces the 21-credit cap.
     *
     * @param student the student registering
     * @param course  the course to register for
     * @throws CreditLimitException if registering would exceed 21 credits
     */
    public void registerCourse(Student student, Course course) throws CreditLimitException {
        if (student == null || course == null) return;
        student.registerCourse(course);     // throws CreditLimitException if limit exceeded
        course.enrollStudent(student);      // also enroll on the course side
        System.out.println("[REGISTRATION] " + student.getLogin()
                + " registered for " + course.getCode()
                + " (total credits: " + student.getCredits() + ")");
    }

    /**
     * Manager-approved registration flow.
     * If CreditLimitException occurs, it is caught and logged — manager
     * can still force-enroll if business rules allow, but here we report and stop.
     *
     * @param manager the manager approving
     * @param student the student
     * @param course  the course
     */
    public void approveRegistration(Manager manager, Student student, Course course) {
        if (manager == null || student == null || course == null) return;
        try {
            registerCourse(student, course);
            System.out.println("[REGISTRATION] Approved by " + manager.getLogin());
        } catch (CreditLimitException e) {
            System.out.println("[REGISTRATION] Approval failed: " + e.getMessage());
        }
    }

    /**
     * Check whether adding a course would stay within the 21-credit cap.
     *
     * @param student   the student to check
     * @param newCourse the course being considered
     * @return true if registration is allowed
     */
    public boolean checkCreditLimit(Student student, Course newCourse) {
        if (student == null || newCourse == null) return false;
        return (student.getCredits() + newCourse.getCredits()) <= Student.MAX_CREDITS;
    }

    /**
     * Drop (withdraw from) a course.
     * Removes the course from the student's list and reduces their credit count.
     *
     * @param student the student withdrawing
     * @param course  the course to drop
     */
    public void dropCourse(Student student, Course course) {
        if (student == null || course == null) return;
        if (student.getCourses().remove(course)) {
            student.setCredits(student.getCredits() - course.getCredits());
            course.removeStudent(student);
            System.out.println("[REGISTRATION] " + student.getLogin()
                    + " dropped course " + course.getCode());
        }
    }
}
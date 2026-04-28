package users;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class Student extends User {

    /**
     * Default constructor
     */
    public Student() {
    }

    /**
     * 
     */
    private double gpa;

    /**
     * 
     */
    private int credits;

    /**
     * 
     */
    private void studentType;

    /**
     * 
     */
    private List<Course> courses;

    /**
     * 
     */
    private int failedCount;

    /**
     * 
     */
    private List<StudentOrganization> organizations;




    /**
     * @param c 
     * @return
     */
    public void registerCourse(Course c) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Mark> viewMarks() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Transcript getTranscript() {
        // TODO implement here
        return null;
    }

    /**
     * @param t 
     * @param rating 
     * @return
     */
    public void rateTeacher(Teacher t, int rating) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Course> viewCourses() {
        // TODO implement here
        return null;
    }

    /**
     * @param org 
     * @return
     */
    public void joinOrganization(StudentOrganization org) {
        // TODO implement here
        return null;
    }

    /**
     * @param s 
     * @return
     */
    public int compareTo(Student s) {
        // TODO implement here
        return 0;
    }

}
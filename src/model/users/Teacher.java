package users;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class Teacher extends Employee {

    /**
     * Default constructor
     */
    public Teacher() {
    }

    /**
     * 
     */
    private void position;

    /**
     * 
     */
    private List<Course> courses;

    /**
     * 
     */
    private double rating;

    /**
     * 
     */
    private int ratingCount;


    /**
     * @param s 
     * @param c 
     * @param mark 
     * @return
     */
    public void putMark(void s, Course c, Mark mark) {
        // TODO implement here
        return null;
    }

    /**
     * @param c 
     * @return
     */
    public List<Student> viewStudents(Course c) {
        // TODO implement here
        return null;
    }

    /**
     * @param s 
     * @param level 
     * @return
     */
    public void sendComplaint(void s, UrgencyLevel level) {
        // TODO implement here
        return null;
    }

    /**
     * @param c 
     * @return
     */
    public void manageCourse(Course c) {
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
     * @param c 
     * @return
     */
    public String generateMarksReport(Course c) {
        // TODO implement here
        return "";
    }

}
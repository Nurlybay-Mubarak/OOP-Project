
import UniversitySystem.model.users.Teacher;
import UniversitySystem.model.users.Student;
import UniversitySystem.model.academic.Course;
import UniversitySystem.model.academic.Mark;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class MarkController {

    /**
     * Default constructor
     */
    public MarkController() {
    }

    /**
     * @param teacher 
     * @param student 
     * @param course 
     * @param mark 
     * @return
     */
    public void putMark(Teacher teacher, Student student, Course course, Mark mark) {
        // TODO implement here
        return null;
    }

    /**
     * @param student 
     * @return
     */
    public List<Mark> getMarks(Student student) {
        // TODO implement here
        return null;
    }

    /**
     * @param student 
     * @return
     */
    public double calculateGpa(Student student) {
        // TODO implement here
        return 0.0d;
    }

}
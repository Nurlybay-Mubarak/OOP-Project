package model.academic;

import enums.CourseType;
import model.users.Student;
import model.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a university Course.
 * A course has a code, name, credit value, type (MAJOR/MINOR/ELECTIVE),
 * an enrollment cap, and lists of assigned teachers, lessons, and students.
 */
public class Course implements Comparable<Course>, Serializable {

    private static final long serialVersionUID = 1L;

    private String        code;
    private String        name;
    private int           credits;
    private CourseType    courseType;
    private List<Teacher> teachers;
    private List<Lesson>  lessons;
    private int           maxStudents;
    private List<Student> enrolledStudents;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Course() {
        this.teachers         = new ArrayList<>();
        this.lessons          = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    public Course(String code, String name, int credits,
                  CourseType courseType, int maxStudents) {
        this.code             = code;
        this.name             = name;
        this.credits          = credits;
        this.courseType       = courseType;
        this.maxStudents      = maxStudents;
        this.teachers         = new ArrayList<>();
        this.lessons          = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public String getCode()              { return code; }
    public void   setCode(String code)   { this.code = code; }

    public String getName()              { return name; }
    public void   setName(String name)   { this.name = name; }

    public int  getCredits()          { return credits; }
    public void setCredits(int c)     { this.credits = c; }

    public CourseType getCourseType()              { return courseType; }
    public void       setCourseType(CourseType ct) { this.courseType = ct; }

    public List<Teacher> getTeachers()                  { return teachers; }
    public void          setTeachers(List<Teacher> t)   { this.teachers = t; }

    public List<Lesson> getLessons()                 { return lessons; }
    public void         setLessons(List<Lesson> l)   { this.lessons = l; }

    public int  getMaxStudents()        { return maxStudents; }
    public void setMaxStudents(int max) { this.maxStudents = max; }

    public List<Student> getEnrolledStudents()                   { return enrolledStudents; }
    public void          setEnrolledStudents(List<Student> list) { this.enrolledStudents = list; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Assign a teacher to this course (no duplicates).
     */
    public void addTeacher(Teacher t) {
        if (t != null && !teachers.contains(t)) {
            teachers.add(t);
        }
    }

    /**
     * Add a lesson to the course schedule (no duplicates).
     */
    public void addLesson(Lesson l) {
        if (l != null && !lessons.contains(l)) {
            lessons.add(l);
        }
    }

    /**
     * Enroll a student if there is capacity and they are not already enrolled.
     *
     * @param s the student to enroll
     * @return true if enrollment succeeded, false if course is full or already enrolled
     */
    public boolean enrollStudent(Student s) {
        if (s == null) return false;
        if (enrolledStudents.size() >= maxStudents) return false;
        if (enrolledStudents.contains(s)) return false;
        enrolledStudents.add(s);
        return true;
    }

    /**
     * Remove a student from the enrolled list (e.g., withdrawal).
     */
    public void removeStudent(Student s) {
        enrolledStudents.remove(s);
    }

    /**
     * Returns a short info string about the course.
     */
    public String getInfo() {
        return "Course[" + code + "] " + name + " (" + credits + " cr, "
                + courseType + ", " + enrolledStudents.size() + "/" + maxStudents + " students)";
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    /** Natural ordering: alphabetical by course code. */
    @Override
    public int compareTo(Course c) {
        return this.code.compareTo(c.code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
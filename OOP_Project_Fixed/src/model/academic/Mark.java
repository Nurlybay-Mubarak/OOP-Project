package model.academic;

import model.users.Student;
import model.users.Teacher;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a student's mark for a specific course.
 * Total = att1 + att2 + finalExam (max 100 points).
 * Letter grade thresholds follow standard GPA scale.
 */
public class Mark implements Serializable {

    private static final long serialVersionUID = 1L;

    private double  att1;
    private double  att2;
    private double  finalExam;
    private Course  course;
    private Student student;
    private Teacher assignedBy;   // the teacher who assigned this mark

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Mark() {
    }

    public Mark(Course course, Student student) {
        this.course   = course;
        this.student  = student;
        this.att1     = 0;
        this.att2     = 0;
        this.finalExam = 0;
    }

    public Mark(Course course, Student student, double att1, double att2, double finalExam) {
        this.course    = course;
        this.student   = student;
        this.att1      = att1;
        this.att2      = att2;
        this.finalExam = finalExam;
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public double getAtt1()          { return att1; }
    public void   setAtt1(double v)  { this.att1 = clamp(v, 0, 30); }

    public double getAtt2()          { return att2; }
    public void   setAtt2(double v)  { this.att2 = clamp(v, 0, 30); }

    public double getFinalExam()          { return finalExam; }
    public void   setFinalExam(double v)  { this.finalExam = clamp(v, 0, 40); }

    public Course getCourse()             { return course; }
    public void   setCourse(Course c)     { this.course = c; }

    public Student getStudent()           { return student; }
    public void    setStudent(Student s)  { this.student = s; }

    public Teacher getAssignedBy()            { return assignedBy; }
    public void    setAssignedBy(Teacher t)   { this.assignedBy = t; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /** Returns the total score (att1 + att2 + finalExam). */
    public double getTotal() {
        return att1 + att2 + finalExam;
    }

    /**
     * Returns the letter grade based on the total score.
     * Scale: A(95+), A-(90), B+(85), B(80), B-(75), C+(70),
     *        C(65), C-(60), D+(55), D(50), F(<50)
     */
    public String getLetterGrade() {
        double total = getTotal();
        if (total >= 95) return "A";
        if (total >= 90) return "A-";
        if (total >= 85) return "B+";
        if (total >= 80) return "B";
        if (total >= 75) return "B-";
        if (total >= 70) return "C+";
        if (total >= 65) return "C";
        if (total >= 60) return "C-";
        if (total >= 55) return "D+";
        if (total >= 50) return "D";
        return "F";
    }

    /** Returns true if the student passed this course (total >= 50). */
    public boolean isPassed() {
        return getTotal() >= 50;
    }

    // ------------------------------------------------------------------ //
    //  Helpers
    // ------------------------------------------------------------------ //

    private static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return Objects.equals(course, mark.course) && Objects.equals(student, mark.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, student);
    }

    @Override
    public String toString() {
        return "Mark{course=" + (course != null ? course.getCode() : "null")
                + ", total=" + getTotal()
                + ", grade='" + getLetterGrade() + "'}";
    }
}
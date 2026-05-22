package model.academic;

import model.users.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a student's official academic transcript.
 * Stores all marks and computes the cumulative GPA on a 4.0 scale.
 */
public class Transcript implements Serializable {

    private static final long serialVersionUID = 1L;

    private Student    student;
    private double     gpa;
    private List<Mark> marks;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Transcript() {
        this.marks = new ArrayList<>();
        this.gpa   = 0.0;
    }

    public Transcript(Student student) {
        this.student = student;
        this.gpa     = 0.0;
        this.marks   = new ArrayList<>();
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public Student    getStudent()            { return student; }
    public void       setStudent(Student s)   { this.student = s; }

    public double     getGpa()                { return gpa; }
    public void       setGpa(double gpa)      { this.gpa = gpa; }

    public List<Mark> getMarks()              { return marks; }
    public void       setMarks(List<Mark> m)  { this.marks = m; recalculate(); }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Add a mark to this transcript (no duplicates for same course).
     * Recalculates GPA after addition.
     *
     * @param mark the mark to add
     */
    public void addMark(Mark mark) {
        if (mark == null) return;
        // Replace existing mark for the same course if present
        marks.removeIf(m -> Objects.equals(m.getCourse(), mark.getCourse()));
        marks.add(mark);
        recalculate();
    }

    /**
     * Calculate and return the GPA based on current marks (4.0 scale).
     *
     * @return the GPA value
     */
    public double calculateGpa() {
        if (marks.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Mark m : marks) {
            sum += toGpaPoints(m.getTotal());
        }
        return Math.round((sum / marks.size()) * 100.0) / 100.0;
    }

    /**
     * Generate a printable transcript report.
     *
     * @return formatted string with all marks and GPA
     */
    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== TRANSCRIPT ==========\n");
        sb.append("Student: ").append(student != null ? student.toString() : "Unknown").append("\n");
        sb.append("---------------------------------\n");
        for (Mark m : marks) {
            sb.append(String.format("  %-10s | Total: %5.1f | Grade: %-3s%n",
                    m.getCourse() != null ? m.getCourse().getCode() : "?",
                    m.getTotal(),
                    m.getLetterGrade()));
        }
        sb.append("---------------------------------\n");
        sb.append(String.format("  GPA: %.2f%n", gpa));
        sb.append("=================================\n");
        return sb.toString();
    }

    // ------------------------------------------------------------------ //
    //  Helpers
    // ------------------------------------------------------------------ //

    private void recalculate() {
        this.gpa = calculateGpa();
    }

    /**
     * Convert numeric total to GPA points using standard university scale.
     */
    private static double toGpaPoints(double total) {
        if (total >= 95) return 4.0;
        if (total >= 90) return 3.67;
        if (total >= 85) return 3.33;
        if (total >= 80) return 3.0;
        if (total >= 75) return 2.67;
        if (total >= 70) return 2.33;
        if (total >= 65) return 2.0;
        if (total >= 60) return 1.67;
        if (total >= 55) return 1.33;
        if (total >= 50) return 1.0;
        return 0.0;
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transcript that = (Transcript) o;
        return Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student);
    }

    @Override
    public String toString() {
        return generate();
    }
}
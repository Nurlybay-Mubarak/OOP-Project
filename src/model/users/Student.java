package model.users;

import model.academic.Course;
import model.academic.Mark;
import model.academic.StudentOrganization;
import model.academic.Transcript;
import enums.StudentType;
import exceptions.CreditLimitException;
import exceptions.TooManyFailsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a university Student.
 * A student can register for courses (subject to the 21-credit cap),
 * view marks, access their transcript, rate teachers, and join student organisations.
 * Implements Comparable<Student> to support GPA-based ranking.
 */
public class Student extends User implements Comparable<Student>, Serializable {

    private static final long serialVersionUID = 1L;

    /** Maximum credits a student may take in one semester. */
    public static final int MAX_CREDITS = 21;

    /** Threshold at which TooManyFailsException should be considered. */
    public static final int MAX_FAILS = 3;

    private double                 gpa;
    private int                    credits;
    private StudentType            studentType;
    private List<Course>           courses;
    private int                    failedCount;
    private List<StudentOrganization> organizations;
    private Transcript             transcript;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Student() {
        super();
        this.courses       = new ArrayList<>();
        this.organizations = new ArrayList<>();
        this.transcript    = new Transcript(this);
        this.gpa           = 0.0;
        this.credits       = 0;
        this.failedCount   = 0;
    }

    public Student(String login, String password, String firstName,
                   String lastName, String email, StudentType studentType) {
        super(login, password, firstName, lastName, email);
        this.studentType   = studentType;
        this.courses       = new ArrayList<>();
        this.organizations = new ArrayList<>();
        this.transcript    = new Transcript(this);
        this.gpa           = 0.0;
        this.credits       = 0;
        this.failedCount   = 0;
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public double getGpa()              { return gpa; }
    public void   setGpa(double gpa)    { this.gpa = gpa; }

    public int  getCredits()            { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public StudentType getStudentType()                  { return studentType; }
    public void        setStudentType(StudentType type)  { this.studentType = type; }

    public List<Course> getCourses()                { return courses; }
    public void         setCourses(List<Course> c)  { this.courses = c; }

    public int  getFailedCount()               { return failedCount; }
    public void setFailedCount(int count)      { this.failedCount = count; }

    public List<StudentOrganization> getOrganizations() { return organizations; }
    public void setOrganizations(List<StudentOrganization> orgs) { this.organizations = orgs; }

    public Transcript getTranscript()              { return transcript; }
    public void       setTranscript(Transcript t)  { this.transcript = t; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Register for a course if the credit limit allows it.
     * Throws CreditLimitException if adding this course would exceed MAX_CREDITS.
     *
     * @param c the course to register for
     * @throws CreditLimitException when the 21-credit cap would be exceeded
     */
    public void registerCourse(Course c) throws CreditLimitException {
        if (c == null) return;
        if (courses.contains(c)) {
            System.out.println("[WARN] Already registered for: " + c.getCode());
            return;
        }
        if (this.credits + c.getCredits() > MAX_CREDITS) {
            throw new CreditLimitException(
                "Cannot register for '" + c.getName() + "': would exceed "
                + MAX_CREDITS + "-credit limit. Current credits: " + this.credits
                + ", course credits: " + c.getCredits());
        }
        this.courses.add(c);
        this.credits += c.getCredits();
    }

    /**
     * View all marks from the transcript.
     *
     * @return list of Mark objects
     */
    public List<Mark> viewMarks() {
        return transcript.getMarks();
    }

    /**
     * Returns the student's transcript object.
     * (Explicit method as required by the UML model.)
     */
    public Transcript viewTranscript() {
        return transcript;
    }

    /**
     * Rate a teacher by submitting a numeric rating.
     * Delegates to Teacher.addRating().
     *
     * @param t      the teacher to rate
     * @param rating integer from 1 to 5
     */
    public void rateTeacher(Teacher t, int rating) {
        if (t == null) return;
        if (rating < 1 || rating > 5) {
            System.out.println("[WARN] Rating must be between 1 and 5.");
            return;
        }
        t.addRating(rating);
    }

    /**
     * Returns all courses this student is registered for.
     */
    public List<Course> viewCourses() {
        return courses;
    }

    /**
     * Join a student organisation.
     *
     * @param org the organisation to join
     */
    public void joinOrganization(StudentOrganization org) {
        if (org == null) return;
        if (!organizations.contains(org)) {
            organizations.add(org);
            org.addMember(this);
        }
    }

    /**
     * Record a failed course attempt.
     * Throws TooManyFailsException if this push exceeds MAX_FAILS.
     *
     * @throws TooManyFailsException when failedCount > MAX_FAILS
     */
    public void recordFail() throws TooManyFailsException {
        this.failedCount++;
        if (this.failedCount > MAX_FAILS) {
            throw new TooManyFailsException(
                getLogin() + " has failed " + failedCount
                + " courses — exceeds allowed maximum of " + MAX_FAILS + ".");
        }
    }

    /**
     * Refresh GPA from the current transcript data.
     */
    public void refreshGpa() {
        this.gpa = transcript.calculateGpa();
    }

    // ------------------------------------------------------------------ //
    //  Comparable Implementation
    // ------------------------------------------------------------------ //

    /** Natural ordering: by GPA descending (highest GPA first). */
    @Override
    public int compareTo(Student other) {
        return Double.compare(other.getGpa(), this.gpa);
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Student{login='" + getLogin() + "', name='" + getFirstName()
                + " " + getLastName() + "', type=" + studentType
                + ", gpa=" + gpa + ", credits=" + credits + "}";
    }
}
package model.users;

import enums.CitationFormat;
import enums.TeacherPosition;
import enums.UrgencyLevel;
import exceptions.NotResearcherException;
import model.academic.Course;
import model.academic.Mark;
import model.research.ResearchPaper;
import model.research.Researcher;
import model.support.SupportRequest;
import patterns.ResearchPaperComparator;
import storage.DataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a university Teacher.
 * A Teacher extends Employee and optionally implements Researcher
 * (any teacher can publish papers after being assigned a Researcher role).
 */
public class Teacher extends Employee implements Researcher, Serializable {

    private static final long serialVersionUID = 1L;

    private TeacherPosition    position;
    private List<Course>       courses;
    private double             rating;
    private int                ratingCount;
    private List<ResearchPaper> papers;
    private boolean            isResearcher;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Teacher() {
        super();
        this.courses     = new ArrayList<>();
        this.papers      = new ArrayList<>();
        this.rating      = 0.0;
        this.ratingCount = 0;
        this.isResearcher = false;
    }

    public Teacher(String login, String password, String firstName,
                   String lastName, String email, double salary,
                   TeacherPosition position) {
        super(login, password, firstName, lastName, email, salary);
        this.position    = position;
        this.courses     = new ArrayList<>();
        this.papers      = new ArrayList<>();
        this.rating      = 0.0;
        this.ratingCount = 0;
        // Professors are always researchers per specification
        this.isResearcher = (position == TeacherPosition.PROFESSOR);
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public TeacherPosition getPosition()                  { return position; }
    public void            setPosition(TeacherPosition p) {
        this.position = p;
        // If promoted to Professor, automatically become a researcher
        if (p == TeacherPosition.PROFESSOR) {
            this.isResearcher = true;
        }
    }

    public boolean isResearcher()                { return isResearcher; }
    public void    setResearcher(boolean flag)    { this.isResearcher = flag; }

    public List<Course> getCourses()               { return courses; }
    public void         setCourses(List<Course> c) { this.courses = c; }

    public double getRating()            { return rating; }
    public void   setRating(double r)    { this.rating = r; }

    public int  getRatingCount()         { return ratingCount; }
    public void setRatingCount(int c)    { this.ratingCount = c; }

    // ------------------------------------------------------------------ //
    //  Teaching Methods
    // ------------------------------------------------------------------ //

    /**
     * Submit a cumulative rating from a student.
     * Recalculates the running average.
     *
     * @param newRating integer 1-5 submitted by a student
     */
    public void addRating(int newRating) {
        double total = this.rating * this.ratingCount + newRating;
        this.ratingCount++;
        this.rating = Math.round((total / this.ratingCount) * 100.0) / 100.0;
    }

    /**
     * Assign a mark to a student for a specific course.
     * Also updates the student's transcript.
     *
     * @param s    the student
     * @param c    the course
     * @param mark the mark to assign
     */
    public void putMark(Student s, Course c, Mark mark) {
        if (s == null || c == null || mark == null) return;
        mark.setStudent(s);
        mark.setCourse(c);
        s.getTranscript().addMark(mark);
        s.refreshGpa();
        if (!mark.isPassed()) {
            try {
                s.recordFail();
            } catch (exceptions.TooManyFailsException e) {
                System.out.println("[ALERT] " + e.getMessage());
            }
        }
    }

    /**
     * Returns the list of students enrolled in the given course.
     *
     * @param c the course to inspect
     * @return list of enrolled students
     */
    public List<Student> viewStudents(Course c) {
        if (c == null) return Collections.emptyList();
        return c.getEnrolledStudents();
    }

    /**
     * Assign this teacher to manage a course (added to teacher's course list).
     *
     * @param c the course to manage
     */
    public void manageCourse(Course c) {
        if (c != null && !courses.contains(c)) {
            courses.add(c);
            c.addTeacher(this);
        }
    }

    /**
     * Returns the list of courses this teacher is assigned to.
     */
    public List<Course> viewCourses() {
        return courses;
    }

    /**
     * Generate a textual report of marks for a given course.
     *
     * @param c the course
     * @return formatted report string
     */
    public String generateMarksReport(Course c) {
        if (c == null) return "No course specified.";
        StringBuilder sb = new StringBuilder();
        sb.append("=== Marks Report: ").append(c.getName()).append(" ===\n");
        for (Student s : c.getEnrolledStudents()) {
            List<Mark> marks = s.viewMarks();
            marks.stream()
                 .filter(m -> Objects.equals(m.getCourse(), c))
                 .forEach(m -> sb.append(String.format("  %-20s | %s | %.1f%n",
                         s.getLastName() + " " + s.getFirstName(),
                         m.getLetterGrade(), m.getTotal())));
        }
        return sb.toString();
    }

    /**
     * Send a formal complaint about a student (stored as a SupportRequest).
     *
     * @param s       the student in question
     * @param urgency urgency level of the complaint
     */
    public void sendComplaint(Student s, UrgencyLevel urgency) {
        if (s == null) return;
        String desc = "Complaint about student: " + s.getLogin();
        SupportRequest req = new SupportRequest(this, desc, urgency);
        DataStore.getInstance().addSupportRequest(req);
        System.out.println("[COMPLAINT] " + getLogin() + " filed complaint against " + s.getLogin());
    }

    // ------------------------------------------------------------------ //
    //  Researcher Interface Implementation
    // ------------------------------------------------------------------ //

    @Override
    public void publishPaper(ResearchPaper paper) {
        if (paper == null) return;
        if (!isResearcher) {
            System.out.println("[WARN] " + getLogin()
                    + " is not a researcher (position: " + position
                    + "). Cannot publish papers. Use setResearcher(true) to enable.");
            return;
        }
        if (!papers.contains(paper)) {
            paper.setAuthor(getFirstName() + " " + getLastName());
            papers.add(paper);
            DataStore.getInstance().addResearchPaper(paper);
            System.out.println("[RESEARCH] " + getLogin() + " published: " + paper.getTitle());
        }
    }

    @Override
    public int calculateHIndex() {
        if (papers.isEmpty()) return 0;
        List<Integer> citations = new ArrayList<>();
        for (ResearchPaper p : papers) {
            citations.add(p.getCitations());
        }
        Collections.sort(citations, Collections.reverseOrder());
        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }

    @Override
    public List<ResearchPaper> printPapers(ResearchPaperComparator comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(comparator::compare);
        return sorted;
    }

    @Override
    public List<ResearchPaper> getPapers() {
        return papers;
    }

    @Override
    public String getCitation(ResearchPaper paper, CitationFormat format) {
        if (paper == null) return "";
        return paper.getCitation(format);
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Teacher{login='" + getLogin() + "', name='" + getFirstName()
                + " " + getLastName() + "', position=" + position
                + ", rating=" + rating + "}";
    }
}
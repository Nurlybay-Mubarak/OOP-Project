package model.users;

import enums.CitationFormat;
import enums.StudentType;
import exceptions.LowHIndexException;
import model.research.ResearchPaper;
import model.research.Researcher;
import patterns.ResearchPaperComparator;
import storage.DataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a graduate (Master or PhD) student.
 * Extends Student and also implements the Researcher interface,
 * because graduate students can publish papers and have an H-Index.
 * A graduate student must have a supervisor (Researcher) with H-Index >= 3.
 */
public class GraduateStudent extends Student implements Researcher, Serializable {

    private static final long serialVersionUID = 1L;

    private Researcher         supervisor;
    private List<ResearchPaper> papers;
    private List<ResearchPaper> diplomaPapers;  // diploma/thesis papers

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public GraduateStudent() {
        super();
        setStudentType(StudentType.MASTER);
        this.papers       = new ArrayList<>();
        this.diplomaPapers = new ArrayList<>();
    }

    public GraduateStudent(String login, String password, String firstName,
                           String lastName, String email, StudentType studentType) {
        super(login, password, firstName, lastName, email, studentType);
        this.papers       = new ArrayList<>();
        this.diplomaPapers = new ArrayList<>();
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public Researcher getSupervisor()              { return supervisor; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Assign a supervisor to this graduate student.
     * The supervisor must have an H-Index of at least 3.
     *
     * @param r the researcher to assign as supervisor
     * @throws LowHIndexException if the supervisor's H-Index is below 3
     */
    public void setSupervisor(Researcher r) throws LowHIndexException {
        if (r == null) throw new IllegalArgumentException("Supervisor cannot be null.");
        int hIndex = r.calculateHIndex();
        if (hIndex < 3) {
            throw new LowHIndexException(
                "Supervisor's H-Index (" + hIndex + ") is below the required minimum of 3.");
        }
        this.supervisor = r;
        System.out.println("[SUPERVISOR] " + getLogin()
                + " assigned supervisor with H-Index=" + hIndex);
    }

    /**
     * Alias for setSupervisor — as modelled in the UML diagram.
     */
    public void assignSupervisor(Researcher r) throws LowHIndexException {
        setSupervisor(r);
    }

    /**
     * Submit a diploma (thesis) paper.
     * Stored separately in diplomaPapers list (and also published via publishPaper).
     */
    public void submitDiplomaPaper(ResearchPaper paper) {
        if (paper == null) return;
        if (!diplomaPapers.contains(paper)) diplomaPapers.add(paper);
        publishPaper(paper);   // also adds to general papers list
    }

    /** Returns only diploma/thesis papers. */
    public List<ResearchPaper> getDiplomaPapers() {
        return java.util.Collections.unmodifiableList(diplomaPapers);
    }

    // ------------------------------------------------------------------ //
    //  Researcher Interface Implementation
    // ------------------------------------------------------------------ //

    @Override
    public void publishPaper(ResearchPaper paper) {
        if (paper == null) return;
        if (!papers.contains(paper)) {
            paper.setAuthor(getFirstName() + " " + getLastName());
            papers.add(paper);
            DataStore.getInstance().addResearchPaper(paper);
            System.out.println("[RESEARCH] GradStudent " + getLogin()
                    + " published: " + paper.getTitle());
        }
    }

    @Override
    public int calculateHIndex() {
        if (papers.isEmpty()) return 0;
        List<Integer> citations = new ArrayList<>();
        for (ResearchPaper p : papers) citations.add(p.getCitations());
        Collections.sort(citations, Collections.reverseOrder());
        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) h = i + 1; else break;
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
    public List<ResearchPaper> getPapers() { return papers; }

    @Override
    public String getCitation(ResearchPaper paper, CitationFormat format) {
        return paper != null ? paper.getCitation(format) : "";
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() { return super.hashCode(); }

    @Override
    public String toString() {
        return "GraduateStudent{login='" + getLogin() + "', type="
                + getStudentType() + ", gpa=" + getGpa()
                + ", papers=" + papers.size() + "}";
    }
}
package controller;

import enums.CitationFormat;
import enums.School;
import exceptions.LowHIndexException;
import exceptions.NotResearcherException;
import model.research.Journal;
import model.research.ResearchPaper;
import model.research.Researcher;
import model.research.ResearchProject;
import model.users.User;
import patterns.ResearchPaperComparator;
import storage.DataStore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles all research-related operations:
 * publishing papers, computing H-Index, managing journals and projects.
 */
public class ResearchController {

    public ResearchController() {}

    /**
     * Publish a paper through a researcher.
     * Also creates a news announcement automatically.
     *
     * @param researcher the researcher publishing
     * @param paper      the paper to publish
     */
    public void publishPaper(Researcher researcher, ResearchPaper paper) {
        if (researcher == null || paper == null) return;
        researcher.publishPaper(paper);
        new NewsController().createResearchAnnouncement(paper);
    }

    /**
     * Publish a paper to a specific journal.
     * The journal notifies all subscribers (Observer pattern).
     *
     * @param researcher the researcher publishing
     * @param paper      the paper
     * @param journal    the target journal
     */
    public void publishPaperToJournal(Researcher researcher, ResearchPaper paper, Journal journal) {
        if (researcher == null || paper == null || journal == null) return;
        researcher.publishPaper(paper);
        journal.publishPaper(paper);   // triggers notifyObservers() → all subscribers get update()
    }

    /**
     * Calculate the H-Index for a researcher.
     *
     * @param researcher the researcher
     * @return computed H-Index
     */
    public int calculateHIndex(Researcher researcher) {
        if (researcher == null) return 0;
        return researcher.calculateHIndex();
    }

    /**
     * Have a user join a research project.
     * Throws NotResearcherException if the user is not a Researcher.
     *
     * @param user    the user wishing to join
     * @param project the target project
     * @throws NotResearcherException if the user cannot act as a researcher
     */
    public void joinProject(User user, ResearchProject project) throws NotResearcherException {
        if (user == null || project == null) return;
        if (!(user instanceof Researcher)) {
            throw new NotResearcherException(user.getLogin()
                    + " is not a researcher and cannot join research projects.");
        }
        project.addParticipant(user.getId());
        System.out.println("[RESEARCH] " + user.getLogin()
                + " joined project: " + project.getName());
    }

    /**
     * Return the top researchers sorted by H-Index descending.
     *
     * @return sorted list of Researcher objects
     */
    public List<Researcher> printTopCitedResearchers() {
        List<Researcher> researchers = new ArrayList<>();
        DataStore.getInstance().getAllTeachers().stream()
                .filter(t -> t.isResearcher())
                .forEach(researchers::add);
        DataStore.getInstance().getAllGraduateStudents().forEach(researchers::add);
        researchers.sort((r1, r2) ->
                Integer.compare(r2.calculateHIndex(), r1.calculateHIndex()));
        return researchers;
    }

    /**
     * Return all papers sorted by the given comparator strategy.
     *
     * @param comparator the sorting strategy
     * @return sorted list of papers
     */
    public List<ResearchPaper> getPapersSorted(ResearchPaperComparator comparator) {
        List<ResearchPaper> all = new ArrayList<>(DataStore.getInstance().getAllResearchPapers());
        all.sort(comparator::compare);
        return all;
    }

    /**
     * Get the citation for a specific paper in the requested format.
     *
     * @param paper  the paper to cite
     * @param format PLAIN_TEXT or BIBTEX
     * @return formatted citation string
     */
    public String getCitation(ResearchPaper paper, CitationFormat format) {
        if (paper == null) return "";
        return paper.getCitation(format);
    }

    // ------------------------------------------------------------------ //
    //  University-wide Research Paper Printing
    // ------------------------------------------------------------------ //

    /**
     * Collect ALL papers from ALL researchers in the university
     * and return them sorted by the given comparator.
     * This satisfies the requirement: "printing research papers of
     * all researchers in the university, sorted by date/citations/pages".
     *
     * @param comparator the sorting strategy
     * @return sorted list of all research papers in the university
     */
    public List<ResearchPaper> printAllUniversityPapers(ResearchPaperComparator comparator) {
        List<ResearchPaper> allPapers = new ArrayList<>(
                DataStore.getInstance().getAllResearchPapers());
        allPapers.sort(comparator::compare);
        System.out.println("=== All University Research Papers ===");
        for (ResearchPaper p : allPapers) {
            System.out.println("  " + p.getTitle()
                    + " | Author: " + p.getAuthor()
                    + " | Citations: " + p.getCitations()
                    + " | Pages: " + p.getPages());
        }
        System.out.println("  Total: " + allPapers.size() + " papers");
        return allPapers;
    }

    // ------------------------------------------------------------------ //
    //  Top Cited Researcher by School
    // ------------------------------------------------------------------ //

    /**
     * Find the top cited researcher within a specific school (faculty).
     *
     * @param school the school to filter by
     * @return the researcher with the highest H-Index in that school, or null
     */
    public Researcher getTopCitedResearcherBySchool(School school) {
        List<Researcher> researchers = getAllResearchersInSchool(school);
        if (researchers.isEmpty()) return null;
        return researchers.stream()
                .max(Comparator.comparingInt(Researcher::calculateHIndex))
                .orElse(null);
    }

    /**
     * Print the top cited researcher for each school.
     */
    public void printTopCitedResearcherBySchool() {
        System.out.println("=== Top Cited Researcher per School ===");
        for (School school : School.values()) {
            Researcher top = getTopCitedResearcherBySchool(school);
            if (top != null && top.calculateHIndex() > 0) {
                String name = (top instanceof User)
                        ? ((User) top).getFirstName() + " " + ((User) top).getLastName()
                        : top.toString();
                System.out.println("  " + school + ": " + name
                        + " (H-Index: " + top.calculateHIndex() + ")");
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Top Cited Researcher of the Year
    // ------------------------------------------------------------------ //

    /**
     * Find the top cited researcher of a given year across all schools.
     * Considers only papers published in that year.
     *
     * @param year the year to check (e.g. 2026)
     * @return the researcher with the most citations on papers from that year
     */
    public Researcher getTopCitedResearcherOfYear(int year) {
        List<Researcher> allResearchers = new ArrayList<>();
        DataStore.getInstance().getAllTeachers().stream()
                .filter(t -> t.isResearcher())
                .forEach(allResearchers::add);
        DataStore.getInstance().getAllGraduateStudents().forEach(allResearchers::add);

        Researcher topResearcher = null;
        int maxCitations = 0;

        for (Researcher r : allResearchers) {
            int yearCitations = 0;
            for (ResearchPaper p : r.getPapers()) {
                if (p.getPublishedDate() != null && getYearFromDate(p.getPublishedDate()) == year) {
                    yearCitations += p.getCitations();
                }
            }
            if (yearCitations > maxCitations) {
                maxCitations = yearCitations;
                topResearcher = r;
            }
        }
        return topResearcher;
    }

    /**
     * Print the top cited researcher of a given year.
     *
     * @param year the year to report on
     */
    public void printTopCitedResearcherOfYear(int year) {
        Researcher top = getTopCitedResearcherOfYear(year);
        if (top != null) {
            String name = (top instanceof User)
                    ? ((User) top).getFirstName() + " " + ((User) top).getLastName()
                    : top.toString();
            System.out.println("=== Top Cited Researcher of " + year + " ===");
            System.out.println("  " + name + " (H-Index: " + top.calculateHIndex() + ")");
        } else {
            System.out.println("  No researchers found for year " + year);
        }
    }

    // ------------------------------------------------------------------ //
    //  Helpers
    // ------------------------------------------------------------------ //

    /**
     * Get all researchers belonging to a specific school.
     */
    private List<Researcher> getAllResearchersInSchool(School school) {
        List<Researcher> result = new ArrayList<>();
        DataStore.getInstance().getAllTeachers().stream()
                .filter(t -> t.isResearcher() && t.getSchool() == school)
                .forEach(result::add);
        DataStore.getInstance().getAllGraduateStudents().stream()
                .filter(g -> g.getSchool() == school)
                .forEach(result::add);
        return result;
    }

    @SuppressWarnings("deprecation")
    private int getYearFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
}
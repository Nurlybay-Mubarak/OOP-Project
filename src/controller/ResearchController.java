package controller;

import enums.CitationFormat;
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
import java.util.Comparator;
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
        DataStore.getInstance().getAllTeachers().forEach(researchers::add);
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
}
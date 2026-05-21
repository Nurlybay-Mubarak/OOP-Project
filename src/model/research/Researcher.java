package model.research;

import enums.CitationFormat;
import exceptions.LowHIndexException;
import patterns.ResearchPaperComparator;

import java.util.List;

/**
 * Interface that marks a user as a Researcher.
 * Any class implementing this interface gains research-related capabilities.
 * Both Teacher and GraduateStudent can act as Researchers.
 */
public interface Researcher {

    /**
     * Publish a new research paper (adds it to the researcher's paper list and to the journal).
     *
     * @param paper the paper to publish
     */
    void publishPaper(ResearchPaper paper);

    /**
     * Calculate the H-Index for this researcher.
     * H-Index = h, where h papers have at least h citations each.
     *
     * @return the computed H-Index value
     */
    int calculateHIndex();

    /**
     * Return all papers sorted by the given comparator strategy.
     *
     * @param comparator a concrete ResearchPaperComparator strategy
     * @return sorted list of papers
     */
    List<ResearchPaper> printPapers(ResearchPaperComparator comparator);

    /**
     * Return the full list of papers authored by this researcher.
     *
     * @return list of ResearchPaper objects
     */
    List<ResearchPaper> getPapers();

    /**
     * Format a specific paper's citation using the given format.
     *
     * @param paper  the paper to cite
     * @param format PLAIN_TEXT or BIBTEX
     * @return formatted citation string
     */
    String getCitation(ResearchPaper paper, CitationFormat format);
}

package patterns;

import model.research.ResearchPaper;

/**
 * Concrete Strategy: sorts ResearchPapers by publication date (newest first).
 */
public class ResearchPaperDateComparator implements ResearchPaperComparator {

    /**
     * Compares two papers by their publication date in descending order (newest first).
     *
     * @param p1 the first paper
     * @param p2 the second paper
     * @return negative if p1 was published after p2
     */
    @Override
    public int compare(ResearchPaper p1, ResearchPaper p2) {
        if (p1.getPublishedDate() == null && p2.getPublishedDate() == null) return 0;
        if (p1.getPublishedDate() == null) return 1;
        if (p2.getPublishedDate() == null) return -1;
        return p2.getPublishedDate().compareTo(p1.getPublishedDate());
    }
}
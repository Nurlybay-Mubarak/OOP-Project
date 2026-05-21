package patterns;

import model.research.ResearchPaper;

/**
 * Strategy interface for comparing two ResearchPaper objects.
 * Concrete implementations define different sorting criteria.
 */
public interface ResearchPaperComparator {
    /**
     * Compare two ResearchPaper objects.
     *
     * @param p1 the first paper
     * @param p2 the second paper
     * @return negative if p1 < p2, 0 if equal, positive if p1 > p2
     */
    int compare(ResearchPaper p1, ResearchPaper p2);
}
package patterns;

import model.research.ResearchPaper;

/**
 * Concrete Strategy: sorts ResearchPapers by number of pages (ascending).
 */
public class ResearchPaperPagesComparator implements ResearchPaperComparator {

    /**
     * Compares two papers by their page count in ascending order.
     *
     * @param p1 the first paper
     * @param p2 the second paper
     * @return negative if p1 has fewer pages
     */
    @Override
    public int compare(ResearchPaper p1, ResearchPaper p2) {
        return Integer.compare(p1.getPages(), p2.getPages());
    }
}
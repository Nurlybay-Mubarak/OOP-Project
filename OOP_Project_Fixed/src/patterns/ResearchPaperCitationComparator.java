package patterns;

import model.research.ResearchPaper;

/**
 * Concrete Strategy: sorts ResearchPapers by citation count (descending — most cited first).
 */
public class ResearchPaperCitationComparator implements ResearchPaperComparator {

    /**
     * Compares two papers by their citation count in descending order.
     *
     * @param p1 the first paper
     * @param p2 the second paper
     * @return positive if p1 has fewer citations than p2 (p2 ranks higher)
     */
    @Override
    public int compare(ResearchPaper p1, ResearchPaper p2) {
        return Integer.compare(p2.getCitations(), p1.getCitations());
    }
}
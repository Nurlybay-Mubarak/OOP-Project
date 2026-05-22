package patterns;

import model.research.ResearchPaper;

/**
 * Observer interface — part of the Observer design pattern.
 * Any class that wants to receive notifications when a new ResearchPaper
 * is published in a Journal must implement this interface.
 */
public interface Observer {
    /**
     * Called by the Subject (Journal) when a new paper is published.
     *
     * @param p the newly published ResearchPaper
     */
    void update(ResearchPaper p);
}
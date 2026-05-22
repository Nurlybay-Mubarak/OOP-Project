package patterns;

import model.research.ResearchPaper;

/**
 * Subject interface — part of the Observer design pattern.
 * A Journal acts as the Subject: it maintains a list of Observers (subscribers)
 * and notifies them whenever a new paper is published.
 */
public interface Subject {
    /**
     * Register an observer to receive future notifications.
     *
     * @param o the observer to add
     */
    void subscribe(Observer o);

    /**
     * Remove an observer so it no longer receives notifications.
     *
     * @param o the observer to remove
     */
    void unsubscribe(Observer o);

    /**
     * Notify all registered observers about a newly published paper.
     *
     * @param p the paper that was just published
     */
    void notifyObservers(ResearchPaper p);
}
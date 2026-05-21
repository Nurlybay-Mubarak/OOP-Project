package model.research;

import patterns.Observer;
import patterns.Subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a scientific journal that publishes ResearchPaper objects.
 * Implements the Subject side of the Observer pattern:
 *   — researchers subscribe to be notified of new papers.
 *   — when publishPaper() is called, all subscribers receive an update().
 */
public class Journal implements Subject, Serializable {

    private static final long serialVersionUID = 1L;

    private String              name;
    private List<Observer>      subscribers;
    private List<ResearchPaper> papers;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Journal() {
        this.subscribers = new ArrayList<>();
        this.papers      = new ArrayList<>();
    }

    public Journal(String name) {
        this.name        = name;
        this.subscribers = new ArrayList<>();
        this.papers      = new ArrayList<>();
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public String getName()              { return name; }
    public void   setName(String name)   { this.name = name; }

    public List<ResearchPaper> getPapers() { return Collections.unmodifiableList(papers); }

    // ------------------------------------------------------------------ //
    //  Subject (Observer Pattern) Implementation
    // ------------------------------------------------------------------ //

    @Override
    public void subscribe(Observer o) {
        if (!subscribers.contains(o)) {
            subscribers.add(o);
        }
    }

    @Override
    public void unsubscribe(Observer o) {
        subscribers.remove(o);
    }

    @Override
    public void notifyObservers(ResearchPaper p) {
        for (Observer observer : subscribers) {
            observer.update(p);
        }
    }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Publish a paper in this journal.
     * Adds the paper to the journal's list and notifies all subscribers.
     *
     * @param p the paper to publish
     */
    public void publishPaper(ResearchPaper p) {
        if (!papers.contains(p)) {
            p.setJournalName(this.name);
            papers.add(p);
            notifyObservers(p);
        }
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Journal journal = (Journal) o;
        return Objects.equals(name, journal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Journal{name='" + name + "', papers=" + papers.size() + "}";
    }
}
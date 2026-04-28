package research;

import UniversitySystem.patterns.Subject;
import UniversitySystem.patterns.Observer;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class Journal implements Subject {

    /**
     * Default constructor
     */
    public Journal() {
    }

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private List<Observer> subscribers;

    /**
     * 
     */
    private List<ResearchPaper> papers;






    /**
     * @param o 
     * @return
     */
    public void subscribe(Observer o) {
        // TODO implement here
        return null;
    }

    /**
     * @param o 
     * @return
     */
    public void unsubscribe(Observer o) {
        // TODO implement here
        return null;
    }

    /**
     * @param p 
     * @return
     */
    public void publishPaper(ResearchPaper p) {
        // TODO implement here
        return null;
    }

    /**
     * @param p
     */
    public void notifyObservers(ResearchPaper p) {
        // TODO implement here
    }

    /**
     * @param o 
     * @return
     */
    public void subscribe(Observer o) {
        // TODO implement Subject.subscribe() here
        return null;
    }

    /**
     * @param o 
     * @return
     */
    public void unsubscribe(Observer o) {
        // TODO implement Subject.unsubscribe() here
        return null;
    }

    /**
     * @param p 
     * @return
     */
    public void notifyObservers(ResearchPaper p) {
        // TODO implement Subject.notifyObservers() here
        return null;
    }

}
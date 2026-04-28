
import java.io.*;
import java.util.*;

/**
 * 
 */
public interface Subject {

    /**
     * @param o 
     * @return
     */
    public void subscribe(Observer o);

    /**
     * @param o 
     * @return
     */
    public void unsubscribe(Observer o);

    /**
     * @param p 
     * @return
     */
    public void notifyObservers(ResearchPaper p);

}
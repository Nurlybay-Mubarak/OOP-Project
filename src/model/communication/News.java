
import UniversitySystem.enums.NewsTopic;
import UniversitySystem.model.users.User;

import java.io.*;
import java.util.*;

/**
 * 
 */
public class News {

    /**
     * Default constructor
     */
    public News() {
    }

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private NewsTopic topic;

    /**
     * 
     */
    private Date date;

    /**
     * 
     */
    private User author;

    /**
     * 
     */
    private List<Comment> comments;

    /**
     * 
     */
    private boolean isPinned;





    /**
     * @param c 
     * @return
     */
    public void addComment(Comment c) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void pin() {
        // TODO implement here
        return null;
    }

}
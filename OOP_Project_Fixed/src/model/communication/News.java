package model.communication;

import enums.NewsTopic;
import model.users.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Represents a news/announcement post visible to all users of the system.
 * Managers create and pin news; users can add comments.
 */
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    private String       title;
    private String       content;
    private NewsTopic    topic;
    private Date         date;
    private User         author;
    private List<Comment> comments;
    private boolean      isPinned;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public News() {
        this.comments = new ArrayList<>();
        this.date     = new Date();
        this.isPinned = false;
    }

    public News(String title, String content, NewsTopic topic, User author) {
        this.title    = title;
        this.content  = content;
        this.topic    = topic;
        this.author   = author;
        this.date     = new Date();
        this.comments = new ArrayList<>();
        this.isPinned = false;
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public String getTitle()              { return title; }
    public void   setTitle(String title)  { this.title = title; }

    public String getContent()                { return content; }
    public void   setContent(String content)  { this.content = content; }

    public NewsTopic getTopic()              { return topic; }
    public void      setTopic(NewsTopic t)   { this.topic = t; }

    public Date getDate()          { return date; }
    public void setDate(Date date) { this.date = date; }

    public User getAuthor()           { return author; }
    public void setAuthor(User a)     { this.author = a; }

    public List<Comment> getComments()  { return Collections.unmodifiableList(comments); }

    public boolean isPinned()              { return isPinned; }
    public void    setPinned(boolean flag) { this.isPinned = flag; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Add a comment to this news post.
     *
     * @param c the comment to add
     */
    public void addComment(Comment c) {
        if (c != null) {
            comments.add(c);
        }
    }

    /**
     * Pin this news post so it appears at the top of the news feed.
     */
    public void pin() {
        this.isPinned = true;
    }

    /**
     * Unpin this news post.
     */
    public void unpin() {
        this.isPinned = false;
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(title, news.title) && Objects.equals(date, news.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date);
    }

    @Override
    public String toString() {
        return "News{" + (isPinned ? "[PINNED] " : "") + "title='" + title
                + "', topic=" + topic + ", date=" + date + "}";
    }
}
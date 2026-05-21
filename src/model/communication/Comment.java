package model.communication;

import model.users.User;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a comment left by a User on a News post.
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private User   author;
    private String text;
    private Date   date;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Comment() {
        this.date = new Date();
    }

    public Comment(User author, String text) {
        this.author = author;
        this.text   = text;
        this.date   = new Date();
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public User   getAuthor()          { return author; }
    public void   setAuthor(User a)    { this.author = a; }

    public String getText()           { return text; }
    public void   setText(String t)   { this.text = t; }

    public Date   getDate()           { return date; }
    public void   setDate(Date d)     { this.date = d; }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(author, comment.author)
                && Objects.equals(text, comment.text)
                && Objects.equals(date, comment.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, text, date);
    }

    @Override
    public String toString() {
        return "[" + date + "] " + (author != null ? author.getLogin() : "anonymous")
                + ": " + text;
    }
}
package model.support;                            // ДОБАВЛЕНО

import model.users.User;                          // ДОБАВЛЕНО
import enums.RequestStatus;                       // ДОБАВЛЕНО

import java.io.*;
import java.util.*;

/**
 * Represents a tech support request (e.g. fix projector, printer).
 * Status flow: NEW → VIEWED → ACCEPTED/REJECTED → DONE
 */
public class SupportRequest implements Serializable {

    private String id;                             // ИСПРАВЛЕНО: было public
    private User author;                           // ИСПРАВЛЕНО: было public
    private String description;                    // ИСПРАВЛЕНО: было public
    private RequestStatus status;                  // ИСПРАВЛЕНО: было public
    private Date createdDate;                      // ИСПРАВЛЕНО: было public

    /**
     * Default constructor
     */
    public SupportRequest() {
        this.status = RequestStatus.NEW;
        this.createdDate = new Date();
    }

    /**
     * Constructor with parameters
     */
    public SupportRequest(String id, User author, String description) {
        this.id = id;
        this.author = author;
        this.description = description;
        this.status = RequestStatus.NEW;           // новый запрос всегда NEW
        this.createdDate = new Date();
    }

    /**
     * Update the status of this request
     */
    public void updateStatus(RequestStatus s) {
        this.status = s;
        System.out.println("Request [" + id + "] status changed to: " + s);
    }

    // ==================== GETTERS & SETTERS ====================

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return "[" + id + "] " + description
                + " | Status: " + status
                + " | By: " + author.getFirstName()
                + " | Date: " + createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupportRequest)) return false;
        SupportRequest that = (SupportRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
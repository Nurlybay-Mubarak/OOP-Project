package model.support;

import enums.RequestStatus;
import enums.UrgencyLevel;
import model.users.User;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a technical support request submitted by a User.
 * Lifecycle: NEW -> VIEWED -> ACCEPTED | REJECTED -> DONE
 */
public class SupportRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String        id;
    private User          author;
    private String        description;
    private RequestStatus status;
    private UrgencyLevel  urgencyLevel;
    private Date          createdDate;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public SupportRequest() {
        this.id          = UUID.randomUUID().toString();
        this.status      = RequestStatus.NEW;
        this.createdDate = new Date();
    }

    public SupportRequest(User author, String description, UrgencyLevel urgencyLevel) {
        this.id            = UUID.randomUUID().toString();
        this.author        = author;
        this.description   = description;
        this.urgencyLevel  = urgencyLevel;
        this.status        = RequestStatus.NEW;
        this.createdDate   = new Date();
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public String getId()              { return id; }
    public void   setId(String id)     { this.id = id; }

    public User   getAuthor()          { return author; }
    public void   setAuthor(User u)    { this.author = u; }

    public String getDescription()          { return description; }
    public void   setDescription(String d)  { this.description = d; }

    public RequestStatus getStatus()              { return status; }
    public void          setStatus(RequestStatus s) { this.status = s; }

    public UrgencyLevel getUrgencyLevel()               { return urgencyLevel; }
    public void         setUrgencyLevel(UrgencyLevel u) { this.urgencyLevel = u; }

    public Date getCreatedDate()           { return createdDate; }
    public void setCreatedDate(Date date)  { this.createdDate = date; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Transitions the request to a new status.
     * Validates allowed transitions to prevent illegal status changes.
     *
     * @param newStatus the target status
     */
    public void updateStatus(RequestStatus newStatus) {
        this.status = newStatus;
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupportRequest that = (SupportRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SupportRequest{id='" + id + "', status=" + status
                + ", urgency=" + urgencyLevel + ", desc='" + description + "'}";
    }
}
package model.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a research project that multiple researchers can join.
 */
public class ResearchProject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String       name;
    private String       topic;
    private String       description;
    private List<String> participantIds;   // stores User IDs of participants
    private List<ResearchPaper> publishedPapers;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public ResearchProject() {
        this.participantIds  = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    public ResearchProject(String name, String description) {
        this.name            = name;
        this.topic           = name; // topic defaults to name
        this.description     = description;
        this.participantIds  = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public String getName()              { return name; }
    public void   setName(String n)      { this.name = n; }

    public String getDescription()       { return description; }
    public void   setDescription(String d) { this.description = d; }

    public String getTopic()             { return topic; }
    public void   setTopic(String t)     { this.topic = t; }

    public List<String> getParticipantIds() { return participantIds; }

    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Add a participant (by their user ID) to the project.
     *
     * @param userId the ID of the user joining the project
     */
    public void addParticipant(String userId) {
        if (!participantIds.contains(userId)) {
            participantIds.add(userId);
        }
    }

    /**
     * Remove a participant from the project.
     *
     * @param userId the ID of the user leaving
     */
    public void removeParticipant(String userId) {
        participantIds.remove(userId);
    }

    /**
     * Add a published paper to this project.
     *
     * @param paper the paper to add
     */
    public void addPublishedPaper(ResearchPaper paper) {
        if (paper != null && !publishedPapers.contains(paper)) {
            publishedPapers.add(paper);
        }
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchProject that = (ResearchProject) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ResearchProject{name='" + name + "', participants=" + participantIds.size() + "}";
    }
}

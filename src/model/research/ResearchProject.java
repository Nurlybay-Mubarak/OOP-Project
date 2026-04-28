package model.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResearchProject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String topic;
    private List<Researcher> participants = new ArrayList<>();
    private List<ResearchPaper> publishedPapers = new ArrayList<>();
    private Date startDate;

    public ResearchProject() {
        this.startDate = new Date();
    }

    public ResearchProject(String topic) {
        this.topic = topic;
        this.startDate = new Date();
    }

    public void addParticipant(Researcher researcher) {
        if (researcher != null && !participants.contains(researcher)) {
            participants.add(researcher);
        }
    }

    public void addPaper(ResearchPaper paper) {
        if (paper != null) {
            publishedPapers.add(paper);
        }
    }

    public String getInfo() {
        return "ResearchProject{" +
                "topic='" + topic + '\'' +
                ", participants=" + participants.size() +
                ", papers=" + publishedPapers.size() +
                ", startDate=" + startDate +
                '}';
    }

    public String getTopic() {
        return topic;
    }

    public List<Researcher> getParticipants() {
        return participants;
    }

    public List<ResearchPaper> getPublishedPapers() {
        return publishedPapers;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
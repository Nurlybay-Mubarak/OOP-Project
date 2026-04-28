package model.research;

import java.util.Comparator;
import java.util.List;

public interface Researcher {

    int calculateHIndex();

    void publishPaper(ResearchPaper paper);

    void printPapers(Comparator<ResearchPaper> c);

    List<ResearchPaper> getPapers();

    List<ResearchProject> getProjects();
}
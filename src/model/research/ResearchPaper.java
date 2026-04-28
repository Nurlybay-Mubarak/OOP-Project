package model.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import enums.CitationFormat;

public class ResearchPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private List<Researcher> authors = new ArrayList<>();
    private String journal;
    private int pages;
    private Date date;
    private String doi;
    private int citations;

    public ResearchPaper() {
        this.date = new Date();
    }

    public ResearchPaper(String title, String journal, int pages, Date date, String doi, int citations) {
        this.title = title;
        this.journal = journal;
        this.pages = pages;
        this.date = date;
        this.doi = doi;
        this.citations = citations;
    }

    public void addAuthor(Researcher researcher) {
        if (researcher != null && !authors.contains(researcher)) {
            authors.add(researcher);
        }
    }

    public String getCitation(CitationFormat format) {
        if (format == CitationFormat.BIBTEX) {
            return "@article{" + doi + ", title={" + title + "}, journal={" + journal + "}}";
        }

        return title + ". " + journal + ". DOI: " + doi;
    }

    public String getTitle() {
        return title;
    }

    public List<Researcher> getAuthors() {
        return authors;
    }

    public String getJournal() {
        return journal;
    }

    public int getPages() {
        return pages;
    }

    public Date getDate() {
        return date;
    }

    public String getDoi() {
        return doi;
    }

    public int getCitations() {
        return citations;
    }

    @Override
    public String toString() {
        return "ResearchPaper{" +
                "title='" + title + '\'' +
                ", journal='" + journal + '\'' +
                ", pages=" + pages +
                ", date=" + date +
                ", doi='" + doi + '\'' +
                ", citations=" + citations +
                '}';
    }
}
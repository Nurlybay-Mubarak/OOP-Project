package model.research;

import enums.CitationFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a scientific research paper published by a Researcher.
 * Implements Comparable to support sorting by citation count by default.
 */
public class ResearchPaper implements Comparable<ResearchPaper>, Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String author;          // primary author name
    private int    pages;
    private int    citations;
    private Date   publishedDate;
    private String journalName;
    private String doi;              // Digital Object Identifier
    private String keywords;         // comma-separated keywords

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public ResearchPaper() {
    }

    public ResearchPaper(String title, String author, int pages, Date publishedDate, String journalName) {
        this.title         = title;
        this.author        = author;
        this.pages         = pages;
        this.citations     = 0;
        this.publishedDate = publishedDate;
        this.journalName   = journalName;
        this.doi           = "";
        this.keywords      = "";
    }

    public ResearchPaper(String title, String author, int pages, Date publishedDate,
                         String journalName, String doi, String keywords) {
        this(title, author, pages, publishedDate, journalName);
        this.doi      = doi;
        this.keywords = keywords;
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public String getTitle()         { return title; }
    public void   setTitle(String t) { this.title = t; }

    public String getAuthor()          { return author; }
    public void   setAuthor(String a)  { this.author = a; }

    public int  getPages()       { return pages; }
    public void setPages(int p)  { this.pages = p; }

    public int  getCitations()       { return citations; }
    public void setCitations(int c)  { this.citations = c; }

    /** Increments the citation counter by one. */
    public void addCitation() { this.citations++; }

    public Date getPublishedDate()          { return publishedDate; }
    public void setPublishedDate(Date d)    { this.publishedDate = d; }

    public String getJournalName()           { return journalName; }
    public void   setJournalName(String j)   { this.journalName = j; }

    public String getDoi()               { return doi; }
    public void   setDoi(String doi)     { this.doi = doi; }

    public String getKeywords()              { return keywords; }
    public void   setKeywords(String kw)     { this.keywords = kw; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Returns a formatted citation string for the paper.
     *
     * @param format PLAIN_TEXT or BIBTEX
     * @return formatted citation string
     */
    public String getCitation(CitationFormat format) {
        switch (format) {
            case BIBTEX:
                return "@article{" + sanitizeKey(title) + ",\n"
                        + "  author  = {" + author + "},\n"
                        + "  title   = {" + title + "},\n"
                        + "  journal = {" + journalName + "},\n"
                        + "  pages   = {" + pages + "},\n"
                        + "  year    = {" + getYear() + "},\n"
                        + (doi != null && !doi.isBlank() ? "  doi     = {" + doi + "},\n" : "")
                        + (keywords != null && !keywords.isBlank() ? "  keywords = {" + keywords + "}\n" : "")
                        + "}";
            case PLAIN_TEXT:
            default:
                return author + " (" + getYear() + "). " + title
                        + ". " + journalName + ". Pages: " + pages + ".";
        }
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    /** Default natural ordering: by citations descending (most cited first). */
    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(other.citations, this.citations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchPaper that = (ResearchPaper) o;
        return Objects.equals(title, that.title) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    @Override
    public String toString() {
        return "ResearchPaper{title='" + title + "', author='" + author
                + "', citations=" + citations + ", pages=" + pages + "}";
    }

    // ------------------------------------------------------------------ //
    //  Helpers
    // ------------------------------------------------------------------ //

    private int getYear() {
        if (publishedDate == null) return 0;
        @SuppressWarnings("deprecation")
        int year = publishedDate.getYear() + 1900;
        return year;
    }

    private static String sanitizeKey(String s) {
        if (s == null) return "unknown";
        return s.replaceAll("\\s+", "_").toLowerCase();
    }
}

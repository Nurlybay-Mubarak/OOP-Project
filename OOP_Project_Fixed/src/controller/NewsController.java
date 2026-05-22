package controller;

import enums.NewsTopic;
import model.communication.News;
import model.research.ResearchPaper;
import model.users.Manager;
import storage.DataStore;

import java.util.List;

/**
 * Handles creation, publishing, and pinning of News items.
 */
public class NewsController {

    public NewsController() {}

    /**
     * Create and publish a news article authored by a Manager.
     *
     * @param manager the manager creating the news
     * @param news    the News object to publish
     */
    public void createNews(Manager manager, News news) {
        if (manager == null || news == null) return;
        news.setAuthor(manager);
        DataStore.getInstance().addNews(news);
        System.out.println("[NEWS] Created: '" + news.getTitle()
                + "' by " + manager.getLogin());
    }

    /**
     * Auto-generate a news announcement for a newly published research paper.
     *
     * @param paper the paper that was published
     * @return the generated News item (already added to the DataStore)
     */
    public News createResearchAnnouncement(ResearchPaper paper) {
        if (paper == null) return null;
        String title   = "New Research: " + paper.getTitle();
        String content = "A new research paper has been published: \""
                + paper.getTitle() + "\" by " + paper.getAuthor()
                + " in " + paper.getJournalName() + ".";
        News announcement = new News(title, content, NewsTopic.RESEARCH, null);
        announcement.pin();   // Research news is always pinned per spec
        DataStore.getInstance().addNews(announcement);
        System.out.println("[NEWS] Research announcement (PINNED): " + title);
        return announcement;
    }

    /**
     * Auto-generate a news article about the top cited researcher at the university.
     * Called periodically or after major citation updates.
     *
     * @return the generated News item
     */
    public News generateTopCitedResearcherNews() {
        // Gather all researchers (teachers + graduate students)
        java.util.List<model.research.Researcher> all = new java.util.ArrayList<>();
        DataStore.getInstance().getAllTeachers().forEach(all::add);
        DataStore.getInstance().getAllGraduateStudents().forEach(all::add);
        if (all.isEmpty()) return null;

        // Find the researcher with the highest H-Index
        model.research.Researcher top = all.stream()
                .max(java.util.Comparator.comparingInt(model.research.Researcher::calculateHIndex))
                .orElse(null);
        if (top == null) return null;

        String name = (top instanceof model.users.User)
                ? ((model.users.User) top).getFirstName() + " " + ((model.users.User) top).getLastName()
                : top.toString();
        int hIdx = top.calculateHIndex();

        String title   = "Top Cited Researcher: " + name;
        String content = name + " is the top cited researcher at the university this period "
                + "with an H-Index of " + hIdx + ". Congratulations!";
        News news = new News(title, content, NewsTopic.RESEARCH, null);
        news.pin();   // pinned per spec
        DataStore.getInstance().addNews(news);
        System.out.println("[NEWS] Top Cited Researcher news (PINNED): " + title);
        return news;
    }

    /**
     * Pin a news article so it appears at the top of the feed.
     *
     * @param news the news article to pin
     */
    public void pinResearchNews(News news) {
        if (news == null) return;
        news.pin();
        System.out.println("[NEWS] Pinned: '" + news.getTitle() + "'");
    }

    /**
     * Returns all current news articles from the DataStore.
     */
    public List<News> getAllNews() {
        return DataStore.getInstance().getAllNews();
    }

    /**
     * Returns only pinned news articles.
     */
    public List<News> getPinnedNews() {
        return DataStore.getInstance().getPinnedNews();
    }
}
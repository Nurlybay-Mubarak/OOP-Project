package boundary;

import controller.NewsController;
import controller.ResearchController;
import controller.SupportController;
import enums.CitationFormat;
import enums.UrgencyLevel;
import exceptions.LowHIndexException;
import exceptions.NotResearcherException;
import model.research.ResearchPaper;
import model.research.ResearchProject;
import model.users.GraduateStudent;
import model.users.Teacher;
import patterns.ResearchPaperCitationComparator;
import patterns.ResearchPaperDateComparator;
import patterns.ResearchPaperPagesComparator;
import storage.DataStore;

import java.util.Date;
import java.util.List;

/**
 * Console UI for GraduateStudent — extends StudentBoundary, adds grad + researcher features.
 */
public class GraduateStudentBoundary extends StudentBoundary {

    private final GraduateStudent  gradStudent;
    private final ResearchController resCtrl;
    private final NewsController   newsCtrl;
    private final SupportController supCtrl;

    public GraduateStudentBoundary(GraduateStudent gradStudent) {
        super(gradStudent);
        this.gradStudent = gradStudent;
        this.resCtrl     = new ResearchController();
        this.newsCtrl    = new NewsController();
        this.supCtrl     = new SupportController();
    }

    @Override
    public void showStudentMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══ GRADUATE STUDENT MENU ═══════════════╗");
            System.out.println("║  --- Academic (Student) ---            ║");
            System.out.println("║  1.  View available courses            ║");
            System.out.println("║  2.  Register for a course             ║");
            System.out.println("║  3.  View my marks                     ║");
            System.out.println("║  4.  View transcript                   ║");
            System.out.println("║  5.  Rate a teacher                    ║");
            System.out.println("║  6.  Student organizations             ║");
            System.out.println("║  --- Graduate Specific ---             ║");
            System.out.println("║  7.  View / Set supervisor             ║");
            System.out.println("║  8.  Submit diploma paper              ║");
            System.out.println("║  9.  View diploma papers               ║");
            System.out.println("║  --- Researcher ---                    ║");
            System.out.println("║  10. Publish a research paper          ║");
            System.out.println("║  11. View my papers (sorted)           ║");
            System.out.println("║  12. Get citation                      ║");
            System.out.println("║  13. My H-Index                        ║");
            System.out.println("║  14. Join research project             ║");
            System.out.println("║  15. Top cited researchers             ║");
            System.out.println("║  --- Other ---                         ║");
            System.out.println("║  16. Subscribe to journal              ║");
            System.out.println("║  17. View notifications                ║");
            System.out.println("║  18. Submit support request            ║");
            System.out.println("║  19. Change language                   ║");
            System.out.println("║  0.  Logout                            ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.print("  Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":  showAvailableCourses();   break;
                case "2":  registerCourse();          break;
                case "3":  viewMarks();               break;
                case "4":  viewTranscript();          break;
                case "5":  rateTeacher();             break;
                case "6":  organizationMenu();        break;
                case "7":  supervisorMenu();          break;
                case "8":  submitDiplomaPaper();      break;
                case "9":  viewDiplomaPapers();       break;
                case "10": publishResearchPaper();    break;
                case "11": viewPapersSorted();        break;
                case "12": getCitation();             break;
                case "13": showHIndex();              break;
                case "14": joinResearchProject();     break;
                case "15": topCitedResearchers();     break;
                case "16": subscribeToJournal();      break;
                case "17": viewNotifications();       break;
                case "18": submitSupportRequest();    break;
                case "19": changeLanguage();          break;
                case "0":  running = false;           break;
                default:   System.out.println("  Invalid option.");
            }
        }
    }

    // ── 7. Supervisor ────────────────────────────────────────────────────
    public void supervisorMenu() {
        if (gradStudent.getSupervisor() != null) {
            System.out.println("  Current supervisor: " + gradStudent.getSupervisor());
            System.out.print("  Replace? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) return;
        }
        System.out.print("  Enter supervisor (Teacher) login: ");
        String login = scanner.nextLine().trim();
        DataStore.getInstance().findUserByLogin(login).ifPresentOrElse(
            u -> {
                if (!(u instanceof Teacher)) { System.out.println("  Must be a Teacher."); return; }
                try {
                    gradStudent.setSupervisor((Teacher) u);
                    System.out.println("  Supervisor set: " + u.getLogin()
                            + " (H-Index: " + ((Teacher) u).calculateHIndex() + ")");
                } catch (LowHIndexException e) {
                    System.out.println("  [ERROR] " + e.getMessage());
                }
            },
            () -> System.out.println("  User not found.")
        );
    }

    // ── 8. Submit Diploma Paper ──────────────────────────────────────────
    public void submitDiplomaPaper() {
        System.out.print("  Paper title : "); String title   = scanner.nextLine().trim();
        System.out.print("  Pages       : ");
        int pages;
        try { pages = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { pages = 0; }
        System.out.print("  Journal/Venue: "); String journal = scanner.nextLine().trim();
        ResearchPaper diploma = new ResearchPaper(title,
                gradStudent.getFirstName() + " " + gradStudent.getLastName(),
                pages, new Date(), journal);
        gradStudent.submitDiplomaPaper(diploma);
        System.out.println("  Diploma paper submitted: " + title);
    }

    // ── 9. View Diploma Papers ───────────────────────────────────────────
    public void viewDiplomaPapers() {
        List<ResearchPaper> papers = gradStudent.getDiplomaPapers();
        if (papers.isEmpty()) { System.out.println("  No diploma papers."); return; }
        System.out.println("\n  --- Diploma Papers ---");
        papers.forEach(p -> System.out.printf("  %-35s | pages: %d%n", p.getTitle(), p.getPages()));
    }

    // ── 10. Publish Research Paper ───────────────────────────────────────
    public void publishResearchPaper() {
        System.out.print("  Title   : "); String title   = scanner.nextLine().trim();
        System.out.print("  Pages   : ");
        int pages;
        try { pages = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { pages = 0; }
        System.out.print("  Journal : "); String journal = scanner.nextLine().trim();
        System.out.print("  DOI     : "); String doi     = scanner.nextLine().trim();
        System.out.print("  Keywords: "); String kw      = scanner.nextLine().trim();
        ResearchPaper paper = new ResearchPaper(title,
                gradStudent.getFirstName() + " " + gradStudent.getLastName(),
                pages, new Date(), journal, doi, kw);
        gradStudent.publishPaper(paper);
        newsCtrl.createResearchAnnouncement(paper);
        System.out.println("  Paper published. H-Index: " + gradStudent.calculateHIndex());
    }

    // ── 11. View Papers Sorted ───────────────────────────────────────────
    public void viewPapersSorted() {
        if (gradStudent.getPapers().isEmpty()) { System.out.println("  No papers yet."); return; }
        System.out.println("  Sort by: 1=Date  2=Citations  3=Pages");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        List<ResearchPaper> sorted = gradStudent.printPapers(
            ch.equals("2") ? new ResearchPaperCitationComparator() :
            ch.equals("3") ? new ResearchPaperPagesComparator()    :
                             new ResearchPaperDateComparator());
        sorted.forEach(p -> System.out.printf("  %-35s | cit: %d | pages: %d%n",
            p.getTitle(), p.getCitations(), p.getPages()));
    }

    // ── 12. Get Citation ─────────────────────────────────────────────────
    public void getCitation() {
        List<ResearchPaper> papers = gradStudent.getPapers();
        if (papers.isEmpty()) { System.out.println("  No papers."); return; }
        for (int i = 0; i < papers.size(); i++)
            System.out.println("  [" + i + "] " + papers.get(i).getTitle());
        System.out.print("  Index: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim());
            if (idx < 0 || idx >= papers.size()) { System.out.println("  Invalid."); return; }
            System.out.println("  1=PLAIN_TEXT  2=BIBTEX");
            System.out.print("  Format: ");
            CitationFormat fmt = scanner.nextLine().trim().equals("2")
                    ? CitationFormat.BIBTEX : CitationFormat.PLAIN_TEXT;
            System.out.println(gradStudent.getCitation(papers.get(idx), fmt));
        } catch (NumberFormatException e) { System.out.println("  Invalid input."); }
    }

    // ── 13. H-Index ──────────────────────────────────────────────────────
    public void showHIndex() {
        System.out.println("  H-Index: " + gradStudent.calculateHIndex()
                + "  (papers: " + gradStudent.getPapers().size() + ")");
    }

    // ── 14. Join Research Project ────────────────────────────────────────
    public void joinResearchProject() {
        List<ResearchProject> projects = DataStore.getInstance().getAllResearchProjects();
        if (projects.isEmpty()) { System.out.println("  No research projects."); return; }
        projects.forEach(p -> System.out.println("  " + p.getName()));
        System.out.print("  Project name: ");
        String pName = scanner.nextLine().trim();
        projects.stream().filter(p -> p.getName().equalsIgnoreCase(pName)).findFirst()
            .ifPresentOrElse(
                p -> {
                    try { resCtrl.joinProject(gradStudent, p); System.out.println("  Joined: " + p.getName()); }
                    catch (NotResearcherException e) { System.out.println("  Error: " + e.getMessage()); }
                },
                () -> System.out.println("  Not found.")
            );
    }

    // ── 15. Top Cited Researchers ────────────────────────────────────────
    public void topCitedResearchers() {
        System.out.println("\n  --- Top Cited Researchers ---");
        resCtrl.printTopCitedResearchers()
               .forEach(r -> System.out.println("  H=" + r.calculateHIndex() + " | " + r));
    }

    // Inherited from StudentBoundary: organizationMenu, subscribeToJournal,
    //   viewNotifications, submitSupportRequest, changeLanguage
}

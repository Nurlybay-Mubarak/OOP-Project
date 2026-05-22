package boundary;

import controller.MarkController;
import controller.NewsController;
import controller.ResearchController;
import controller.SupportController;
import enums.CitationFormat;
import enums.Language;
import enums.UrgencyLevel;
import exceptions.CreditLimitException;
import exceptions.NotResearcherException;
import model.academic.Course;
import model.academic.Mark;
import model.research.Journal;
import model.research.ResearchPaper;
import model.research.ResearchProject;
import model.users.Employee;
import model.users.Student;
import model.users.Teacher;
import patterns.ResearchPaperCitationComparator;
import patterns.ResearchPaperDateComparator;
import patterns.ResearchPaperPagesComparator;
import storage.DataStore;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Console UI for Teacher role — full feature coverage including Researcher functions.
 */
public class TeacherBoundary {

    private final Teacher            teacher;
    private final MarkController     markCtrl;
    private final ResearchController resCtrl;
    private final SupportController  supCtrl;
    private final NewsController     newsCtrl;
    private final Scanner            scanner;

    public TeacherBoundary(Teacher teacher) {
        this.teacher  = teacher;
        this.markCtrl = new MarkController();
        this.resCtrl  = new ResearchController();
        this.supCtrl  = new SupportController();
        this.newsCtrl = new NewsController();
        this.scanner  = new Scanner(System.in);
    }

    public void showTeacherMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══ TEACHER MENU ═══════════════════╗");
            System.out.println("║  --- Academic ---                 ║");
            System.out.println("║  1.  View my courses              ║");
            System.out.println("║  2.  View students in course      ║");
            System.out.println("║  3.  Put a mark                   ║");
            System.out.println("║  4.  Generate marks report        ║");
            System.out.println("║  --- Communication ---            ║");
            System.out.println("║  5.  Send message to employee     ║");
            System.out.println("║  6.  Send complaint about student ║");
            System.out.println("║  7.  Submit support request       ║");
            System.out.println("║  --- Research (Researcher) ---    ║");
            System.out.println("║  8.  Publish a paper              ║");
            System.out.println("║  9.  View my papers (sorted)      ║");
            System.out.println("║  10. Get citation                 ║");
            System.out.println("║  11. My H-Index                   ║");
            System.out.println("║  12. Join research project        ║");
            System.out.println("║  13. Top cited researchers        ║");
            System.out.println("║  --- Other ---                    ║");
            System.out.println("║  14. Subscribe to journal         ║");
            System.out.println("║  15. View notifications           ║");
            System.out.println("║  16. Change language              ║");
            System.out.println("║  0.  Logout                       ║");
            System.out.println("╚═══════════════════════════════════╝");
            System.out.print("  Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":  viewCourses();           break;
                case "2":  viewStudentsInCourse();  break;
                case "3":  putMark();               break;
                case "4":  generateReport();        break;
                case "5":  sendMessage();           break;
                case "6":  sendComplaint();         break;
                case "7":  submitSupportRequest();  break;
                case "8":  publishPaper();          break;
                case "9":  if (!checkResearcher()) break; viewPapersSorted();      break;
                case "10": if (!checkResearcher()) break; getCitation();           break;
                case "11": if (!checkResearcher()) break; showHIndex();            break;
                case "12": if (!checkResearcher()) break; joinResearchProject();   break;
                case "13": topCitedResearchers();   break;
                case "14": subscribeToJournal();    break;
                case "15": viewNotifications();     break;
                case "16": changeLanguage();        break;
                case "0":  running = false;         break;
                default:   System.out.println("  Invalid option.");
            }
        }
    }

    // ── 1. View Courses ──────────────────────────────────────────────────
    public void viewCourses() {
        if (teacher.getCourses().isEmpty()) { System.out.println("  No courses assigned."); return; }
        System.out.println("\n  --- My Courses ---");
        teacher.getCourses().forEach(c -> System.out.println("  " + c.getInfo()));
    }

    // ── 2. View Students ─────────────────────────────────────────────────
    public void viewStudentsInCourse() {
        System.out.print("  Course code: ");
        String code = scanner.nextLine().trim();
        DataStore.getInstance().findCourseByCode(code).ifPresentOrElse(
            c -> {
                if (!c.getTeachers().contains(teacher)) { System.out.println("  You are not assigned to this course."); return; }
                System.out.println("  Students in " + c.getCode() + " (" + c.getName() + "):");
                c.getEnrolledStudents().forEach(s ->
                    System.out.printf("  %-20s GPA: %.2f  Credits: %d%n",
                        s.getLastName() + " " + s.getFirstName(), s.getGpa(), s.getCredits()));
            },
            () -> System.out.println("  Course not found.")
        );
    }

    // ── 3. Put Mark ──────────────────────────────────────────────────────
    public void putMark() {
        System.out.print("  Student login: "); String sl = scanner.nextLine().trim();
        System.out.print("  Course code  : "); String cc = scanner.nextLine().trim();
        DataStore.getInstance().findUserByLogin(sl).ifPresentOrElse(
            u -> DataStore.getInstance().findCourseByCode(cc).ifPresentOrElse(
                c -> {
                    if (!(u instanceof Student)) { System.out.println("  Not a student."); return; }
                    try {
                        System.out.print("  ATT1 (0-30): "); double a1 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("  ATT2 (0-30): "); double a2 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("  FINAL(0-40): "); double fe = Double.parseDouble(scanner.nextLine().trim());
                        Mark mark = new Mark(c, (Student) u, a1, a2, fe);
                        markCtrl.putMark(teacher, (Student) u, c, mark);
                        System.out.println("  Mark saved. Grade: " + mark.getLetterGrade() + " (total: " + mark.getTotal() + ")");
                    } catch (Exception e) { System.out.println("  Error: " + e.getMessage()); }
                },
                () -> System.out.println("  Course not found.")),
            () -> System.out.println("  Student not found.")
        );
    }

    // ── 4. Generate Report ───────────────────────────────────────────────
    public void generateReport() {
        System.out.print("  Course code: ");
        String code = scanner.nextLine().trim();
        DataStore.getInstance().findCourseByCode(code).ifPresentOrElse(
            c -> System.out.println(teacher.generateMarksReport(c)),
            () -> System.out.println("  Course not found.")
        );
    }

    // ── 5. Send Message ──────────────────────────────────────────────────
    public void sendMessage() {
        System.out.print("  Recipient login: "); String login = scanner.nextLine().trim();
        System.out.print("  Message text   : "); String text  = scanner.nextLine();
        System.out.print("  Official? (y/n): "); boolean official = scanner.nextLine().trim().equalsIgnoreCase("y");
        DataStore.getInstance().findUserByLogin(login).ifPresentOrElse(
            u -> {
                if (!(u instanceof Employee)) { System.out.println("  Recipient must be an employee."); return; }
                if (official) {
                    model.communication.Message msg = new model.communication.Message(teacher, (Employee) u, text, true);
                    DataStore.getInstance().addMessage(msg);
                    System.out.println("  Official message sent.");
                } else {
                    teacher.sendMessage((Employee) u, text);
                    System.out.println("  Message sent.");
                }
            },
            () -> System.out.println("  User not found.")
        );
    }

    // ── 6. Send Complaint ────────────────────────────────────────────────
    public void sendComplaint() {
        System.out.print("  Student login: "); String login = scanner.nextLine().trim();
        System.out.println("  Urgency: 1=LOW  2=MEDIUM  3=HIGH");
        System.out.print("  Choice: ");
        int uChoice;
        try { uChoice = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { uChoice = 1; }
        final enums.UrgencyLevel level = uChoice == 3 ? enums.UrgencyLevel.HIGH
                                       : uChoice == 2 ? enums.UrgencyLevel.MEDIUM
                                       : enums.UrgencyLevel.LOW;
        DataStore.getInstance().findUserByLogin(login).ifPresentOrElse(
            u -> {
                if (!(u instanceof Student)) { System.out.println("  Not a student."); return; }
                teacher.sendComplaint((Student) u, level);
                System.out.println("  Complaint sent to dean. Urgency: " + level);
            },
            () -> System.out.println("  Student not found.")
        );
    }

    // ── 7. Support Request ───────────────────────────────────────────────
    public void submitSupportRequest() {
        System.out.print("  Issue description: "); String desc = scanner.nextLine().trim();
        System.out.println("  Urgency: 1=LOW  2=MEDIUM  3=HIGH");
        System.out.print("  Choice: ");
        UrgencyLevel level;
        try { int u = Integer.parseInt(scanner.nextLine().trim());
              level = u == 3 ? UrgencyLevel.HIGH : u == 2 ? UrgencyLevel.MEDIUM : UrgencyLevel.LOW;
        } catch (NumberFormatException e) { level = UrgencyLevel.LOW; }
        var req = supCtrl.createRequest(teacher, desc, level);
        System.out.println("  Request created [" + req.getId().substring(0,8) + "] " + req.getStatus());
    }

    // ── 8. Publish Paper ─────────────────────────────────────────────────
    public void publishPaper() {
        if (!teacher.isResearcher()) {
            System.out.println("  You are not a researcher (position: " + teacher.getPosition() + ").");
            System.out.println("  Only Professors and explicitly assigned researchers can publish papers.");
            return;
        }
        System.out.print("  Title   : "); String title = scanner.nextLine().trim();
        System.out.print("  Pages   : ");
        int pages;
        try { pages = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { pages = 0; }
        System.out.print("  Journal : "); String journal = scanner.nextLine().trim();
        System.out.print("  DOI     : "); String doi     = scanner.nextLine().trim();
        System.out.print("  Keywords: "); String kw      = scanner.nextLine().trim();
        ResearchPaper paper = new ResearchPaper(title,
                teacher.getFirstName() + " " + teacher.getLastName(),
                pages, new Date(), journal, doi, kw);
        teacher.publishPaper(paper);
        newsCtrl.createResearchAnnouncement(paper);
        System.out.println("  Paper published! H-Index now: " + teacher.calculateHIndex());
    }

    // ── 9. View Papers Sorted ─────────────────────────────────────────────
    public void viewPapersSorted() {
        if (teacher.getPapers().isEmpty()) { System.out.println("  No papers published yet."); return; }
        System.out.println("  Sort by: 1=Date  2=Citations  3=Pages");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        List<ResearchPaper> sorted = teacher.printPapers(
            ch.equals("2") ? new ResearchPaperCitationComparator() :
            ch.equals("3") ? new ResearchPaperPagesComparator()    :
                             new ResearchPaperDateComparator());
        System.out.println("\n  --- Papers ---");
        sorted.forEach(p -> System.out.printf("  %-35s | cit: %d | pages: %d%n",
            p.getTitle(), p.getCitations(), p.getPages()));
    }

    // ── 10. Get Citation ──────────────────────────────────────────────────
    public void getCitation() {
        List<ResearchPaper> papers = teacher.getPapers();
        if (papers.isEmpty()) { System.out.println("  No papers."); return; }
        for (int i = 0; i < papers.size(); i++)
            System.out.println("  [" + i + "] " + papers.get(i).getTitle());
        System.out.print("  Index: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim());
            if (idx < 0 || idx >= papers.size()) { System.out.println("  Invalid index."); return; }
            System.out.println("  1=PLAIN_TEXT  2=BIBTEX");
            System.out.print("  Format: ");
            CitationFormat fmt = scanner.nextLine().trim().equals("2")
                    ? CitationFormat.BIBTEX : CitationFormat.PLAIN_TEXT;
            System.out.println(teacher.getCitation(papers.get(idx), fmt));
        } catch (NumberFormatException e) { System.out.println("  Invalid input."); }
    }

    // ── 11. H-Index ───────────────────────────────────────────────────────
    public void showHIndex() {
        System.out.println("  Your H-Index: " + teacher.calculateHIndex()
                + "  (papers: " + teacher.getPapers().size() + ")");
    }

    // ── 12. Join Research Project ─────────────────────────────────────────
    public void joinResearchProject() {
        List<ResearchProject> projects = DataStore.getInstance().getAllResearchProjects();
        if (projects.isEmpty()) {
            System.out.print("  No projects. Create new? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) return;
            System.out.print("  Project name: ");        String name = scanner.nextLine().trim();
            System.out.print("  Project topic/desc: ");  String desc = scanner.nextLine().trim();
            ResearchProject rp = new ResearchProject(name, desc);
            DataStore.getInstance().addResearchProject(rp);
            try { resCtrl.joinProject(teacher, rp); System.out.println("  Created & joined: " + name); }
            catch (NotResearcherException e) { System.out.println("  Error: " + e.getMessage()); }
            return;
        }
        projects.forEach(p -> System.out.println("  " + p.getName() + " (" + p.getParticipantIds().size() + " participants)"));
        System.out.print("  Enter project name: "); String pName = scanner.nextLine().trim();
        projects.stream().filter(p -> p.getName().equalsIgnoreCase(pName)).findFirst()
            .ifPresentOrElse(
                p -> {
                    try { resCtrl.joinProject(teacher, p); System.out.println("  Joined: " + p.getName()); }
                    catch (NotResearcherException e) { System.out.println("  Error: " + e.getMessage()); }
                },
                () -> System.out.println("  Project not found.")
            );
    }

    // ── 13. Top Cited Researchers ─────────────────────────────────────────
    public void topCitedResearchers() {
        System.out.println("\n  --- Top Cited Researchers ---");
        resCtrl.printTopCitedResearchers()
               .forEach(r -> System.out.println("  H=" + r.calculateHIndex() + " | " + r));
    }

    // ── 14. Subscribe to Journal ──────────────────────────────────────────
    public void subscribeToJournal() {
        List<Journal> journals = DataStore.getInstance().getAllJournals();
        if (journals.isEmpty()) { System.out.println("  No journals available."); return; }
        journals.forEach(j -> System.out.println("  " + j.getName()));
        System.out.print("  Journal name: "); String name = scanner.nextLine().trim();
        journals.stream().filter(j -> j.getName().equalsIgnoreCase(name)).findFirst()
            .ifPresentOrElse(
                j -> { j.subscribe(teacher); System.out.println("  Subscribed to: " + j.getName()); },
                () -> System.out.println("  Journal not found.")
            );
    }

    // ── 15. View Notifications ────────────────────────────────────────────
    public void viewNotifications() {
        List<model.research.ResearchPaper> notifs = teacher.getNotifications();
        if (notifs.isEmpty()) { System.out.println("  No notifications."); return; }
        System.out.println("\n  --- Notifications (" + notifs.size() + ") ---");
        notifs.forEach(p -> System.out.println("  • New paper: " + p.getTitle()
                + " by " + p.getAuthor()));
    }

    // ── 16. Change Language ───────────────────────────────────────────────
    public void changeLanguage() {
        System.out.println("  1. KZ   2. RU   3. EN");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        enums.Language lang = ch.equals("1") ? enums.Language.KZ : ch.equals("2") ? enums.Language.RU : enums.Language.EN;
        teacher.setLanguage(lang);
        System.out.println("  Language set to: " + lang);
    }

    // ── Helper ──────────────────────────────────────────────────────────
    private boolean checkResearcher() {
        if (!teacher.isResearcher()) {
            System.out.println("  You are not a researcher. This option is unavailable.");
            return false;
        }
        return true;
    }
}
package boundary;

import controller.NewsController;
import controller.ReportController;
import controller.SupportController;
import enums.CourseType;
import enums.ManagerType;
import enums.NewsTopic;
import model.academic.Course;
import model.communication.News;
import model.support.SupportRequest;
import model.users.Manager;
import model.users.Student;
import model.users.Teacher;
import model.users.User;
import storage.DataStore;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Console UI for Manager role — full feature coverage.
 */
public class ManagerBoundary {

    private final Manager          manager;
    private final NewsController   newsCtrl;
    private final ReportController reportCtrl;
    private final SupportController supCtrl;
    private final Scanner          scanner;

    public ManagerBoundary(Manager manager) {
        this.manager    = manager;
        this.newsCtrl   = new NewsController();
        this.reportCtrl = new ReportController();
        this.supCtrl    = new SupportController();
        this.scanner    = new Scanner(System.in);
    }

    public void showManagerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══ MANAGER MENU ════════════════════╗");
            System.out.println("║  --- Courses ---                   ║");
            System.out.println("║  1.  Assign teacher to course      ║");
            System.out.println("║  2.  Approve student registration  ║");
            System.out.println("║  3.  Open course for registration  ║");
            System.out.println("║  --- Students & Teachers ---       ║");
            System.out.println("║  4.  View students (by GPA)        ║");
            System.out.println("║  5.  View students (alphabetically)║");
            System.out.println("║  6.  View all teachers             ║");
            System.out.println("║  --- Requests & News ---           ║");
            System.out.println("║  7.  View employee requests        ║");
            System.out.println("║  8.  Create news                   ║");
            System.out.println("║  9.  View all news                 ║");
            System.out.println("║  10. Pin a news article            ║");
            System.out.println("║  --- Reports ---                   ║");
            System.out.println("║  11. Course marks report           ║");
            System.out.println("║  12. General system report         ║");
            System.out.println("║  --- Communication ---             ║");
            System.out.println("║  13. Send message to employee      ║");
            System.out.println("║  14. Change language               ║");
            System.out.println("║  0.  Logout                        ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("  Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":  assignCourseToTeacher();      break;
                case "2":  approveRegistration();         break;
                case "3":  openCourse();                  break;
                case "4":  viewStudentsByGpa();           break;
                case "5":  viewStudentsAlphabetically();  break;
                case "6":  viewAllTeachers();             break;
                case "7":  viewEmployeeRequests();        break;
                case "8":  createNews();                  break;
                case "9":  viewAllNews();                 break;
                case "10": pinNews();                     break;
                case "11": courseMarksReport();           break;
                case "12": generalReport();               break;
                case "13": sendMessage();                 break;
                case "14": changeLanguage();              break;
                case "0":  running = false;              break;
                default:   System.out.println("  Invalid option.");
            }
        }
    }

    // ── 1. Assign Teacher to Course ──────────────────────────────────────
    public void assignCourseToTeacher() {
        System.out.print("  Course code   : "); String cc = scanner.nextLine().trim();
        System.out.print("  Teacher login : "); String tl = scanner.nextLine().trim();
        DataStore.getInstance().findCourseByCode(cc).ifPresentOrElse(
            c -> DataStore.getInstance().findUserByLogin(tl).ifPresentOrElse(
                u -> {
                    if (!(u instanceof Teacher)) { System.out.println("  Not a teacher."); return; }
                    manager.assignCourseToTeacher(c, (Teacher) u);
                    System.out.println("  " + u.getLogin() + " assigned to " + c.getCode());
                },
                () -> System.out.println("  Teacher not found.")),
            () -> System.out.println("  Course not found.")
        );
    }

    // ── 2. Approve Registration ──────────────────────────────────────────
    public void approveRegistration() {
        System.out.print("  Student login : "); String sl = scanner.nextLine().trim();
        System.out.print("  Course code   : "); String cc = scanner.nextLine().trim();
        DataStore.getInstance().findUserByLogin(sl).ifPresentOrElse(
            u -> DataStore.getInstance().findCourseByCode(cc).ifPresentOrElse(
                c -> {
                    if (!(u instanceof Student)) { System.out.println("  Not a student."); return; }
                    manager.approveRegistration((Student) u, c);
                    System.out.println("  Registration approved.");
                },
                () -> System.out.println("  Course not found.")),
            () -> System.out.println("  Student not found.")
        );
    }

    // ── 3. Open Course for Registration ─────────────────────────────────
    public void openCourse() {
        System.out.print("  Code    : "); String code = scanner.nextLine().trim();
        System.out.print("  Name    : "); String name = scanner.nextLine().trim();
        System.out.print("  Credits : ");
        int cr;
        try { cr = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { cr = 3; }
        System.out.print("  Max students: ");
        int max;
        try { max = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { max = 30; }
        System.out.println("  Type: 1=MAJOR  2=MINOR  3=ELECTIVE");
        System.out.print("  Choice: ");
        String typeCh = scanner.nextLine().trim();
        CourseType type = typeCh.equals("2") ? CourseType.MINOR :
                          typeCh.equals("3") ? CourseType.ELECTIVE : CourseType.MAJOR;
        System.out.print("  Year  : ");
        int year;
        try { year = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { year = 2025; }
        System.out.print("  Major : "); String major = scanner.nextLine().trim();
        Course c = new Course(code, name, cr, type, max);
        manager.openCourseForRegistration(c, year, major);
        System.out.println("  Course opened: " + c.getCode() + " - " + c.getName());
    }

    // ── 4. View Students by GPA ──────────────────────────────────────────
    public void viewStudentsByGpa() {
        List<Student> students = manager.viewStudentsByGpa();
        if (students.isEmpty()) { System.out.println("  No students found."); return; }
        System.out.println("\n  --- Students by GPA (desc) ---");
        students.forEach(s -> System.out.printf("  %-25s GPA: %.2f | Credits: %d%n",
                s.getLastName() + " " + s.getFirstName(), s.getGpa(), s.getCredits()));
    }

    // ── 5. View Students Alphabetically ─────────────────────────────────
    public void viewStudentsAlphabetically() {
        List<Student> students = manager.viewStudentsAlphabetically();
        if (students.isEmpty()) { System.out.println("  No students found."); return; }
        System.out.println("\n  --- Students Alphabetically ---");
        students.forEach(s -> System.out.printf("  %-25s GPA: %.2f%n",
                s.getLastName() + " " + s.getFirstName(), s.getGpa()));
    }

    // ── 6. View All Teachers ─────────────────────────────────────────────
    public void viewAllTeachers() {
        List<Teacher> teachers = manager.viewTeachersInfo();
        if (teachers.isEmpty()) { System.out.println("  No teachers found."); return; }
        System.out.println("\n  --- Teachers ---");
        teachers.forEach(t -> System.out.printf("  %-25s | %-15s | Rating: %.1f | Courses: %d%n",
                t.getLastName() + " " + t.getFirstName(),
                t.getPosition(), t.getRating(), t.getCourses().size()));
    }

    // ── 7. View Employee Requests ─────────────────────────────────────────
    public void viewEmployeeRequests() {
        List<model.support.SupportRequest> requests = manager.viewEmployeeRequests();
        if (requests.isEmpty()) { System.out.println("  No new/viewed requests."); return; }
        System.out.println("\n  --- Employee Requests ---");
        requests.forEach(r -> System.out.printf("  [%s] %-8s | %-8s | %s%n",
                r.getId().substring(0,6), r.getStatus(), r.getUrgencyLevel(), r.getDescription()));
    }

    // ── 8. Create News ───────────────────────────────────────────────────
    public void createNews() {
        System.out.print("  Title  : "); String title   = scanner.nextLine().trim();
        System.out.print("  Content: "); String content = scanner.nextLine();
        System.out.println("  Topic: 1=GENERAL  2=RESEARCH  3=ANNOUNCEMENT");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        NewsTopic topic = ch.equals("2") ? NewsTopic.RESEARCH :
                          ch.equals("3") ? NewsTopic.ANNOUNCEMENT : NewsTopic.GENERAL;
        News n = new News(title, content, topic, manager);
        newsCtrl.createNews(manager, n);
        System.out.println("  News created!" + (topic == NewsTopic.RESEARCH ? " (auto-pinned)" : ""));
    }

    // ── 9. View All News ─────────────────────────────────────────────────
    public void viewAllNews() {
        List<News> news = DataStore.getInstance().getAllNews();
        if (news.isEmpty()) { System.out.println("  No news available."); return; }
        System.out.println("\n  --- News Feed (pinned first) ---");
        news.stream()
            .sorted(Comparator.comparing(News::isPinned).reversed())
            .forEach(n -> System.out.printf("  [%s] %s%n",
                n.isPinned() ? "📌 PINNED" : "       ", n.getTitle()));
    }

    // ── 10. Pin News ─────────────────────────────────────────────────────
    public void pinNews() {
        viewAllNews();
        System.out.print("  Enter title to pin/unpin: ");
        String title = scanner.nextLine().trim();
        DataStore.getInstance().getAllNews().stream()
            .filter(n -> n.getTitle().equalsIgnoreCase(title)).findFirst()
            .ifPresentOrElse(
                n -> {
                    if (n.isPinned()) { n.unpin(); System.out.println("  Unpinned."); }
                    else              { n.pin();   System.out.println("  Pinned!"); }
                },
                () -> System.out.println("  News not found.")
            );
    }

    // ── 11. Course Marks Report ───────────────────────────────────────────
    public void courseMarksReport() {
        System.out.print("  Course code: ");
        String code = scanner.nextLine().trim();
        DataStore.getInstance().findCourseByCode(code).ifPresentOrElse(
            c -> System.out.println(reportCtrl.generateMarksReport(c)),
            () -> System.out.println("  Course not found.")
        );
    }

    // ── 12. General Report ─────────────────────────────────────────────
    public void generalReport() {
        System.out.println(manager.createReport());
        System.out.println(reportCtrl.generateTeacherReport());
    }

    // ── 13. Send Message ─────────────────────────────────────────────────
    public void sendMessage() {
        System.out.print("  Recipient login: "); String login = scanner.nextLine().trim();
        System.out.print("  Message text   : "); String text  = scanner.nextLine();
        DataStore.getInstance().findUserByLogin(login).ifPresentOrElse(
            u -> {
                if (!(u instanceof model.users.Employee)) { System.out.println("  Not an employee."); return; }
                manager.sendMessage((model.users.Employee) u, text);
                System.out.println("  Message sent.");
            },
            () -> System.out.println("  User not found.")
        );
    }

    // ── 14. Change Language ───────────────────────────────────────────────
    public void changeLanguage() {
        System.out.println("  1. KZ   2. RU   3. EN");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        enums.Language lang = ch.equals("1") ? enums.Language.KZ : ch.equals("2") ? enums.Language.RU : enums.Language.EN;
        manager.setLanguage(lang);
        System.out.println("  Language set to: " + lang);
    }
}
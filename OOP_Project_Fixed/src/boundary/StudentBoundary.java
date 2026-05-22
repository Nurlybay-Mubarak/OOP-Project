package boundary;

import controller.CourseRegistrationController;
import controller.MarkController;
import controller.NewsController;
import controller.SupportController;
import enums.Language;
import enums.UrgencyLevel;
import exceptions.CreditLimitException;
import model.academic.Course;
import model.academic.Mark;
import model.academic.StudentOrganization;
import model.research.Journal;
import model.support.SupportRequest;
import model.users.Student;
import model.users.Teacher;
import storage.DataStore;

import java.util.List;
import java.util.Scanner;

/**
 * Console UI for Student role — full feature coverage.
 */
public class StudentBoundary {

    protected final Student                      student;
    private final   CourseRegistrationController regCtrl;
    private final   MarkController               markCtrl;
    private final   SupportController            supCtrl;
    private final   NewsController               newsCtrl;
    protected final Scanner                      scanner;

    public StudentBoundary(Student student) {
        this.student  = student;
        this.regCtrl  = new CourseRegistrationController();
        this.markCtrl = new MarkController();
        this.supCtrl  = new SupportController();
        this.newsCtrl = new NewsController();
        this.scanner  = new Scanner(System.in);
    }

    public void showStudentMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══ STUDENT MENU ══════════════════╗");
            System.out.println("║  1.  View available courses      ║");
            System.out.println("║  2.  Register for a course       ║");
            System.out.println("║  3.  View my marks               ║");
            System.out.println("║  4.  View transcript             ║");
            System.out.println("║  5.  Rate a teacher              ║");
            System.out.println("║  6.  Student organizations       ║");
            System.out.println("║  7.  Subscribe to a journal      ║");
            System.out.println("║  8.  View notifications          ║");
            System.out.println("║  9.  Submit support request      ║");
            System.out.println("║  10. Change language             ║");
            System.out.println("║  11. View teacher info           ║");
            System.out.println("║  0.  Logout                      ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":  showAvailableCourses();   break;
                case "2":  registerCourse();          break;
                case "3":  viewMarks();               break;
                case "4":  viewTranscript();          break;
                case "5":  rateTeacher();             break;
                case "6":  organizationMenu();        break;
                case "7":  subscribeToJournal();      break;
                case "8":  viewNotifications();       break;
                case "9":  submitSupportRequest();    break;
                case "10": changeLanguage();          break;
                case "11": viewTeacherInfo();         break;
                case "0":  running = false;           break;
                default:   System.out.println("  Invalid option.");
            }
        }
    }

    // ── 1. View Courses ──────────────────────────────────────────────────
    public void showAvailableCourses() {
        List<Course> courses = DataStore.getInstance().getAllCourses();
        if (courses.isEmpty()) { System.out.println("  No courses available."); return; }
        System.out.println("\n  --- Available Courses ---");
        courses.forEach(c -> {
            var effectiveType = c.getEffectiveCourseType(student);
            System.out.println("  Course[" + c.getCode() + "] " + c.getName()
                    + " (" + c.getCredits() + " cr, " + effectiveType
                    + ", " + c.getEnrolledStudents().size() + "/" + c.getMaxStudents()
                    + " students)");
        });
    }

    // ── 2. Register for Course ───────────────────────────────────────────
    public void registerCourse() {
        showAvailableCourses();
        System.out.print("  Enter course code: ");
        String code = scanner.nextLine().trim();
        DataStore.getInstance().findCourseByCode(code).ifPresentOrElse(
            course -> {
                try {
                    regCtrl.registerCourse(student, course);
                    System.out.println("  Registered for: " + course.getName());
                } catch (CreditLimitException e) {
                    System.out.println("  [ERROR] " + e.getMessage());
                }
            },
            () -> System.out.println("  Course not found: " + code)
        );
    }

    // ── 3. View Marks ─────────────────────────────────────────────────────
    public void viewMarks() {
        List<Mark> marks = markCtrl.getMarks(student);
        if (marks.isEmpty()) { System.out.println("  No marks yet."); return; }
        System.out.println("\n  --- Your Marks ---");
        marks.forEach(m -> System.out.println("  " + m));
    }

    // ── 4. Transcript ─────────────────────────────────────────────────────
    public void viewTranscript() {
        System.out.println(student.getTranscript().generate());
    }

    // ── 5. Rate Teacher ───────────────────────────────────────────────────
    public void rateTeacher() {
        System.out.print("  Enter teacher login: ");
        String login = scanner.nextLine().trim();
        DataStore.getInstance().findUserByLogin(login).ifPresentOrElse(
            u -> {
                if (!(u instanceof Teacher)) { System.out.println("  Not a teacher."); return; }
                System.out.print("  Rating (1-5): ");
                try {
                    int rating = Integer.parseInt(scanner.nextLine().trim());
                    if (rating < 1 || rating > 5) { System.out.println("  Rating must be 1-5."); return; }
                    student.rateTeacher((Teacher) u, rating);
                    System.out.println("  Rating submitted! Current avg: " + ((Teacher) u).getRating());
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid number.");
                }
            },
            () -> System.out.println("  Teacher not found.")
        );
    }

    // ── 6. Organizations ─────────────────────────────────────────────────
    public void organizationMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  --- Organization Menu ---");
            System.out.println("  a. View my organizations");
            System.out.println("  b. Join an organization");
            System.out.println("  c. Promote member to head");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");
            String ch = scanner.nextLine().trim();
            switch (ch) {
                case "a": viewMyOrganizations();    break;
                case "b": joinOrganization();       break;
                case "c": promoteToHead();          break;
                case "0": back = true;              break;
                default: System.out.println("  Invalid.");
            }
        }
    }

    private void viewMyOrganizations() {
        List<StudentOrganization> orgs = student.getOrganizations();
        if (orgs.isEmpty()) { System.out.println("  No organizations."); return; }
        orgs.forEach(o -> System.out.println("  [" + o.getName() + "]"
                + (o.isHead(student) ? " ← HEAD" : " (member)")));
    }

    private void joinOrganization() {
        List<StudentOrganization> all = DataStore.getInstance().getAllOrganizations();
        if (all.isEmpty()) { System.out.println("  No organizations available."); return; }
        all.forEach(o -> System.out.println("  " + o.getName() + " (members: " + o.getMembers().size() + ")"));
        System.out.print("  Enter organization name: ");
        String name = scanner.nextLine().trim();
        all.stream().filter(o -> o.getName().equalsIgnoreCase(name)).findFirst()
            .ifPresentOrElse(
                o -> { student.joinOrganization(o); System.out.println("  Joined: " + o.getName()); },
                () -> System.out.println("  Organization not found.")
            );
    }

    private void promoteToHead() {
        List<StudentOrganization> orgs = student.getOrganizations();
        if (orgs.isEmpty()) { System.out.println("  You are not in any organization."); return; }
        orgs.forEach(o -> System.out.println("  " + o.getName()));
        System.out.print("  Select organization name: ");
        String orgName = scanner.nextLine().trim();
        orgs.stream().filter(o -> o.getName().equalsIgnoreCase(orgName)).findFirst()
            .ifPresentOrElse(
                o -> {
                    if (!o.isHead(student)) { System.out.println("  You are not the current head."); return; }
                    System.out.print("  Enter new head login: ");
                    String login = scanner.nextLine().trim();
                    DataStore.getInstance().findUserByLogin(login).ifPresentOrElse(
                        u -> { if (u instanceof Student) { o.promoteToHead((Student) u); System.out.println("  Done!"); }
                               else System.out.println("  Not a student."); },
                        () -> System.out.println("  User not found.")
                    );
                },
                () -> System.out.println("  Organization not found.")
            );
    }

    // ── 7. Subscribe to Journal ───────────────────────────────────────────
    public void subscribeToJournal() {
        List<model.research.Journal> journals = DataStore.getInstance().getAllJournals();
        if (journals.isEmpty()) { System.out.println("  No journals available."); return; }
        System.out.println("\n  --- Available Journals ---");
        journals.forEach(j -> System.out.println("  " + j.getName()
                + " | papers: " + j.getPapers().size()));
        System.out.print("  Enter journal name: ");
        String name = scanner.nextLine().trim();
        journals.stream().filter(j -> j.getName().equalsIgnoreCase(name)).findFirst()
            .ifPresentOrElse(
                j -> { j.subscribe(student); System.out.println("  Subscribed to: " + j.getName()); },
                () -> System.out.println("  Journal not found.")
            );
    }

    // ── 8. View Notifications ───────────────────────────────────────────
    public void viewNotifications() {
        List<model.research.ResearchPaper> notifs = student.getNotifications();
        if (notifs.isEmpty()) { System.out.println("  No notifications."); return; }
        System.out.println("\n  --- Notifications (" + notifs.size() + ") ---");
        notifs.forEach(p -> System.out.println("  • New paper published: " + p.getTitle()
                + " by " + p.getAuthor()));
    }

    // ── 9. Support Request ──────────────────────────────────────────────
    public void submitSupportRequest() {
        System.out.print("  Describe the issue: ");
        String desc = scanner.nextLine().trim();
        System.out.println("  Urgency: 1=LOW  2=MEDIUM  3=HIGH");
        System.out.print("  Choice: ");
        UrgencyLevel level;
        try {
            int u = Integer.parseInt(scanner.nextLine().trim());
            level = u == 3 ? UrgencyLevel.HIGH : u == 2 ? UrgencyLevel.MEDIUM : UrgencyLevel.LOW;
        } catch (NumberFormatException e) { level = UrgencyLevel.LOW; }
        SupportRequest req = supCtrl.createRequest(student, desc, level);
        System.out.println("  Request created [" + req.getId().substring(0, 8) + "] - " + req.getStatus());
    }

    // ── 10. Change Language ─────────────────────────────────────────────
    public void changeLanguage() {
        System.out.println("  1. KZ   2. RU   3. EN");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        Language lang = ch.equals("1") ? Language.KZ : ch.equals("2") ? Language.RU : Language.EN;
        student.setLanguage(lang);
        System.out.println("  Language changed to: " + lang);
    }

    // ── 11. View Teacher Info ───────────────────────────────────────────
    public void viewTeacherInfo() {
        System.out.print("  Enter course code to see its teachers: ");
        String code = scanner.nextLine().trim();
        DataStore.getInstance().findCourseByCode(code).ifPresentOrElse(
            c -> {
                if (c.getTeachers().isEmpty()) { System.out.println("  No teachers assigned."); return; }
                System.out.println("\n  Teachers for " + c.getName() + ":");
                c.getTeachers().forEach(t -> System.out.printf("  %-25s | Position: %-15s | Rating: %.1f (%d reviews)%n",
                        t.getLastName() + " " + t.getFirstName(), t.getPosition(), t.getRating(), t.getRatingCount()));
            },
            () -> System.out.println("  Course not found.")
        );
    }
}
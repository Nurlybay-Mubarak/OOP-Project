import boundary.LoginBoundary;
import boundary.MainMenuBoundary;
import controller.*;
import enums.*;
import exceptions.*;
import model.academic.*;
import model.communication.News;
import model.research.*;
import model.support.SupportRequest;
import model.users.*;
import patterns.*;
import storage.DataStore;

import java.util.Date;
import java.util.List;

/**
 * ============================================================
 *  UniversitySystem — Main Simulation Entry Point
 * ============================================================
 *  Demonstrates ALL major features:
 *   1. DataStore (Singleton) population
 *   2. UserFactory (Factory Method) — user creation
 *   3. Authentication (AuthController)
 *   4. Course registration with CreditLimitException
 *   5. Mark assignment and GPA calculation
 *   6. Observer pattern — Journal → User notifications
 *   7. Strategy pattern — ResearchPaperComparator sorting
 *   8. GraduateStudent supervisor with LowHIndexException
 *   9. TechSupport request lifecycle
 *  10. News & report generation
 * ============================================================
 */
public class Main {

    public static void main(String[] args) {

        DataStore db = DataStore.getInstance();

        // Try to load previously saved data
        if (db.loadFromFile()) {
            System.out.println("  Loaded saved data. Skipping demo initialization.");
            System.out.println("  Users: " + db.getAllUsers().size()
                    + ", Courses: " + db.getAllCourses().size()
                    + ", Papers: " + db.getAllResearchPapers().size());
            // Jump straight to CLI
            startCLI(db);
            return;
        }

        // No saved data found — run full demo initialization
        printBanner("1. SYSTEM INITIALISATION — POPULATING DATA STORE");

        // ── Create users via UserFactory ────────────────────────────────
        Admin admin = (Admin) UserFactory.createUser(
                "ADMIN", "admin1", "admin123", "Daniyar", "Seitkali", "admin@uni.kz");

        Teacher teacher = (Teacher) UserFactory.createUser(
                "TEACHER", "teacher1", "teach123", "Asel", "Nurlanovna", "asel@uni.kz");
        ((Teacher) teacher).setPosition(TeacherPosition.PROFESSOR); // Professor → auto-researcher
        teacher.setSchool(School.SITE);

        Teacher teacher2 = (Teacher) UserFactory.createUser(
                "TEACHER", "teacher2", "teach456", "Marat", "Bekovich", "marat@uni.kz");
        teacher2.setSchool(School.SMC);
        // teacher2 is LECTOR → NOT a researcher by default

        Student student1 = (Student) UserFactory.createUser(
                "STUDENT", "student1", "pass123", "Aigerim", "Zhaksybekova", "aigerim@uni.kz");
        student1.setSchool(School.SITE);

        Student student2 = (Student) UserFactory.createUser(
                "STUDENT", "student2", "pass456", "Nurlan", "Askarov", "nurlan@uni.kz");
        student2.setSchool(School.SITE);

        GraduateStudent gradStudent = (GraduateStudent) UserFactory.createUser(
                "GRADUATE", "grad1", "grad123", "Dinara", "Seitkali", "dinara@uni.kz");
        gradStudent.setStudentType(StudentType.PHD);
        gradStudent.setSchool(School.SITE);

        Manager manager = (Manager) UserFactory.createUser(
                "MANAGER", "manager1", "mgr123", "Yerlan", "Ospanov", "yerlan@uni.kz");
        manager.setManagerType(ManagerType.OR);

        TechSupportSpecialist support = (TechSupportSpecialist) UserFactory.createUser(
                "SUPPORT", "support1", "sup123", "Alina", "Bekova", "alina@uni.kz");

        // Register all users
        db.addUser(admin); db.addUser(teacher); db.addUser(teacher2);
        db.addUser(student1); db.addUser(student2); db.addUser(gradStudent);
        db.addUser(manager); db.addUser(support);

        // ── Create courses ───────────────────────────────────────────────
        Course oop   = new Course("CS101", "Object-Oriented Programming", 6, CourseType.MAJOR, 30);
        oop.setSchool(School.SITE);
        Course algo  = new Course("CS201", "Algorithms & Data Structures", 5, CourseType.MAJOR, 25);
        algo.setSchool(School.SITE);
        Course math  = new Course("MA101", "Calculus I",                  4, CourseType.MINOR, 40);
        math.setSchool(School.SMC);
        Course db_c  = new Course("CS301", "Database Systems",            5, CourseType.MAJOR, 20);
        db_c.setSchool(School.SITE);
        Course elec  = new Course("PE101", "Physical Education",          1, CourseType.ELECTIVE, 100);

        // Oil and Gas course — MAJOR for SEG students, ELECTIVE for SITE students
        Course oilGas = new Course("OG201", "Oil & Gas Fundamentals", 4, CourseType.MAJOR, 30);
        oilGas.setSchool(School.SEG);

        // Business School courses — MAJOR for BS students, ELECTIVE for others
        Course finance = new Course("BS101", "Financial Management", 5, CourseType.MAJOR, 30);
        finance.setSchool(School.BS);
        Course marketing = new Course("BS201", "Marketing Strategy", 4, CourseType.MAJOR, 25);
        marketing.setSchool(School.BS);

        db.addCourse(oop); db.addCourse(algo); db.addCourse(math);
        db.addCourse(db_c); db.addCourse(elec); db.addCourse(oilGas);
        db.addCourse(finance); db.addCourse(marketing);

        manager.assignCourseToTeacher(oop,  (Teacher) teacher);
        manager.assignCourseToTeacher(algo, (Teacher) teacher);
        manager.assignCourseToTeacher(math, (Teacher) teacher2);
        manager.assignCourseToTeacher(db_c, (Teacher) teacher);
        db.printSummary();

        // ──────────────────────────────────────────────────────────────────
        printBanner("2. AUTHENTICATION");
        AuthController auth = new AuthController();
        User loggedIn = auth.login("teacher1", "teach123");
        System.out.println("  Authenticated: " + loggedIn);
        auth.changeLanguage(loggedIn, Language.KZ);
        auth.logout(loggedIn);

        User badLogin = auth.login("student1", "WRONG_PASSWORD");
        System.out.println("  Bad login result: " + badLogin);

        // ──────────────────────────────────────────────────────────────────
        printBanner("3. COURSE REGISTRATION (CreditLimitException demo)");
        CourseRegistrationController regCtrl = new CourseRegistrationController();

        try { regCtrl.registerCourse(student1, oop);  } catch (CreditLimitException e) { System.out.println("  ERR: " + e.getMessage()); }
        try { regCtrl.registerCourse(student1, algo); } catch (CreditLimitException e) { System.out.println("  ERR: " + e.getMessage()); }
        try { regCtrl.registerCourse(student1, math); } catch (CreditLimitException e) { System.out.println("  ERR: " + e.getMessage()); }
        try { regCtrl.registerCourse(student1, db_c); } catch (CreditLimitException e) { System.out.println("  ERR: " + e.getMessage()); }

        // This should throw CreditLimitException (6+5+4+5=20, +1 elec=21 ✓, try another 5-credit course)
        Course extra = new Course("CS999", "Extra Heavy Course", 5, CourseType.ELECTIVE, 10);
        db.addCourse(extra);
        try {
            regCtrl.registerCourse(student1, elec);  // +1 => total 21
            regCtrl.registerCourse(student1, extra); // +5 => would be 26 → EXCEPTION
        } catch (CreditLimitException e) {
            System.out.println("  [CAUGHT] CreditLimitException: " + e.getMessage());
        }

        System.out.println("  student1 credits after: " + student1.getCredits());
        System.out.println("  student1 courses: " + student1.getCourses().size());

        // student2 registration
        try { regCtrl.registerCourse(student2, oop);  } catch (CreditLimitException e) {}
        try { regCtrl.registerCourse(student2, algo); } catch (CreditLimitException e) {}

        // ──────────────────────────────────────────────────────────────────
        printBanner("4. MARK ASSIGNMENT & GPA CALCULATION");
        MarkController markCtrl = new MarkController();
        oop.enrollStudent(student1); oop.enrollStudent(student2);
        algo.enrollStudent(student2);

        Mark m1 = new Mark(oop, student1, 28, 27, 38);   // total=93 → A-
        Mark m2 = new Mark(algo, student1, 20, 18, 25);  // total=63 → C (fail)
        Mark m3 = new Mark(oop, student2, 25, 24, 35);   // total=84 → B+

        markCtrl.putMark((Teacher) teacher, student1, oop,  m1);
        markCtrl.putMark((Teacher) teacher, student1, algo, m2);
        markCtrl.putMark((Teacher) teacher, student2, oop,  m3);

        System.out.println("  student1 GPA: " + markCtrl.calculateGpa(student1));
        System.out.println("  student2 GPA: " + markCtrl.calculateGpa(student2));
        System.out.println(student1.getTranscript().generate());

        // ──────────────────────────────────────────────────────────────────
        printBanner("5. TEACHER RATING");
        student1.rateTeacher((Teacher) teacher, 5);
        student2.rateTeacher((Teacher) teacher, 4);
        System.out.println("  " + teacher.getLogin() + " rating: "
                + ((Teacher) teacher).getRating()
                + " (" + ((Teacher) teacher).getRatingCount() + " reviews)");

        // ──────────────────────────────────────────────────────────────────
        printBanner("6. OBSERVER PATTERN — JOURNAL & RESEARCH PAPER NOTIFICATIONS");
        Journal journal1 = new Journal("IEEE Transactions on OOP");
        db.addJournal(journal1);

        // student1 and teacher subscribe to the journal
        journal1.subscribe(student1);
        journal1.subscribe(teacher);

        ResearchController resCtrl = new ResearchController();

        ResearchPaper paper1 = new ResearchPaper("Deep Learning in Education",
                "Asel Nurlanovna", 12, new Date(), "IEEE Transactions on OOP");
        ResearchPaper paper2 = new ResearchPaper("Quantum Algorithms Survey",
                "Asel Nurlanovna", 20, new Date(), "IEEE Transactions on OOP");

        // Publish via journal — triggers Observer update() on all subscribers
        resCtrl.publishPaperToJournal((Teacher) teacher, paper1, journal1);
        resCtrl.publishPaperToJournal((Teacher) teacher, paper2, journal1);

        // Simulate citations
        paper1.addCitation(); paper1.addCitation(); paper1.addCitation();
        paper2.addCitation();

        System.out.println("  teacher H-Index: " + resCtrl.calculateHIndex((Teacher) teacher));
        System.out.println("  student1 notifications: " + student1.getNotifications().size());

        // ──────────────────────────────────────────────────────────────────
        printBanner("7. STRATEGY PATTERN — SORTING RESEARCH PAPERS");
        List<ResearchPaper> byDate = resCtrl.getPapersSorted(new ResearchPaperDateComparator());
        System.out.println("  By Date (newest first):");
        byDate.forEach(p -> System.out.println("    " + p.getTitle() + " [" + p.getCitations() + " cit.]"));

        List<ResearchPaper> byCit = resCtrl.getPapersSorted(new ResearchPaperCitationComparator());
        System.out.println("  By Citations (most cited first):");
        byCit.forEach(p -> System.out.println("    " + p.getTitle() + " [" + p.getCitations() + " cit.]"));

        List<ResearchPaper> byPages = resCtrl.getPapersSorted(new ResearchPaperPagesComparator());
        System.out.println("  By Pages (ascending):");
        byPages.forEach(p -> System.out.println("    " + p.getTitle() + " [" + p.getPages() + " pages]"));

        System.out.println("  Citation (BIBTEX):");
        System.out.println(resCtrl.getCitation(paper1, CitationFormat.BIBTEX));

        // ──────────────────────────────────────────────────────────────────
        printBanner("8. GRADUATE STUDENT — LowHIndexException DEMO");
        // Assign gradStudent to teacher with sufficient H-Index
        try {
            gradStudent.setSupervisor((Teacher) teacher);  // H-Index=2, threshold=3
            System.out.println("  Supervisor assigned successfully.");
        } catch (LowHIndexException e) {
            System.out.println("  [CAUGHT] LowHIndexException: " + e.getMessage());
        }

        // Add more citations to bring teacher's H-Index to 3
        paper1.addCitation(); paper1.addCitation();
        ResearchPaper paper3 = new ResearchPaper("OOP Patterns in Java", "Asel Nurlanovna",
                8, new Date(), "IEEE");
        paper3.setCitations(3);
        ((Teacher) teacher).publishPaper(paper3);

        System.out.println("  Teacher H-Index now: " + resCtrl.calculateHIndex((Teacher) teacher));
        try {
            gradStudent.setSupervisor((Teacher) teacher);
            System.out.println("  Supervisor set: " + gradStudent.getSupervisor());
        } catch (LowHIndexException e) {
            System.out.println("  Still too low: " + e.getMessage());
        }

        // gradStudent publishes a diploma paper
        ResearchPaper diploma = new ResearchPaper("PhD Thesis: AI in Kazakh Education",
                "Dinara Seitkali", 80, new Date(), "KBTU Journal");
        gradStudent.submitDiplomaPaper(diploma);
        System.out.println("  GradStudent papers: " + gradStudent.getPapers().size());

        // ──────────────────────────────────────────────────────────────────
        printBanner("9. TECH SUPPORT REQUEST LIFECYCLE");
        SupportController supCtrl = new SupportController();
        SupportRequest req1 = supCtrl.createRequest(student1,
                "Cannot access my e-journal account.", UrgencyLevel.HIGH);
        SupportRequest req2 = supCtrl.createRequest(teacher,
                "Projector in room 301 is broken.", UrgencyLevel.MEDIUM);

        System.out.println("  New requests: " + supCtrl.viewNewRequests().size());
        supCtrl.viewRequest(req1);
        supCtrl.acceptRequest(support, req1);
        supCtrl.markAsDone(support, req1);
        supCtrl.rejectRequest(support, req2);
        System.out.println("  req1 status: " + req1.getStatus());
        System.out.println("  req2 status: " + req2.getStatus());

        // ──────────────────────────────────────────────────────────────────
        printBanner("10. NEWS & REPORTS");
        NewsController newsCtrl = new NewsController();
        // Regular announcement news
        News n1 = new News("New Semester Starts",
                "Welcome to Spring 2025!", NewsTopic.ANNOUNCEMENT, manager);
        newsCtrl.createNews(manager, n1);

        // Research news is auto-pinned when paper is published (done in step 6)
        // Generate top-cited researcher news automatically
        News topCitedNews = newsCtrl.generateTopCitedResearcherNews();
        System.out.println("  Top cited news created: " + (topCitedNews != null ? topCitedNews.getTitle() : "none"));
        System.out.println("  Pinned news count: " + newsCtrl.getPinnedNews().size());

        ReportController reportCtrl = new ReportController();
        System.out.println(reportCtrl.generateMarksReport(oop));
        System.out.println(reportCtrl.generateStudentReport(student1));
        System.out.println(reportCtrl.generateTeacherReport());

        // GPA Ranking
        System.out.println("  --- Students by GPA (descending) ---");
        manager.viewStudentsByGpa().forEach(s ->
                System.out.printf("  %-20s GPA: %.2f%n",
                        s.getLastName() + " " + s.getFirstName(), s.getGpa()));
        // Alphabetical
        System.out.println("  --- Students Alphabetically ---");
        manager.viewStudentsAlphabetically().forEach(s ->
                System.out.printf("  %s %s%n", s.getLastName(), s.getFirstName()));

        // ──────────────────────────────────────────────────────────────────
        printBanner("11. STUDENT ORGANIZATION (member / head roles)");
        StudentOrganization sciClub = new StudentOrganization("Science Club", student1);
        sciClub.addMember(student2);
        student1.joinOrganization(sciClub);
        student2.joinOrganization(sciClub);

        System.out.println("  " + student1.getLogin() + " is head: " + sciClub.isHead(student1));
        System.out.println("  " + student2.getLogin() + " is member: " + sciClub.isMember(student2));
        // Promote student2 to head
        sciClub.promoteToHead(student2);
        System.out.println("  After promotion — " + student2.getLogin() + " is head: " + sciClub.isHead(student2));

        // ──────────────────────────────────────────────────────────────────
        printBanner("12. OFFICIAL MESSAGES (Working messages between employees)");
        // Teacher sends an official room booking message to manager
        model.communication.Message officialMsg = new model.communication.Message(
                (Teacher) teacher, manager,
                "Official: Room 301 is booked for CS101 final exam on 2025-06-15 at 09:00.", true);
        storage.DataStore.getInstance().addMessage(officialMsg);
        System.out.println("  Official message sent: " + officialMsg);

        // Regular (non-official) message
        teacher.sendMessage(manager, "Reminder: Faculty meeting at 14:00 today.");
        System.out.println("  Messages in system: " + storage.DataStore.getInstance().getAllMessages().size());

        // ──────────────────────────────────────────────────────────────────
        printBanner("13. RESEARCH PAPER — doi & keywords fields");
        ResearchPaper paperWithDoi = new ResearchPaper(
                "Machine Learning for GPA Prediction", "Asel Nurlanovna",
                15, new Date(), "KBTU Journal",
                "10.1145/3456789.0987654", "machine learning, GPA, education, AI");
        ((Teacher) teacher).publishPaper(paperWithDoi);
        System.out.println("  DOI: " + paperWithDoi.getDoi());
        System.out.println("  Keywords: " + paperWithDoi.getKeywords());
        System.out.println(resCtrl.getCitation(paperWithDoi, CitationFormat.BIBTEX));

        // ──────────────────────────────────────────────────────────────────
        printBanner("14. ADMIN OPERATIONS");
        User newTeacher = UserFactory.createUser("TEACHER","teacher3","pass","Sanzhar",
                "Utegenov","sanzhar@uni.kz");
        admin.addUser(newTeacher);
        System.out.println("  Total users now: " + db.getAllUsers().size());
        admin.removeUser(newTeacher);
        System.out.println("  Total users after removal: " + db.getAllUsers().size());
        System.out.println("  Admin logs: " + admin.viewActionLogs());

        // ──────────────────────────────────────────────────────────────────
        printBanner("15. UNIVERSITY-WIDE PAPER PRINTING (sorted)");

        // ── 14b. COURSE TYPE BY SCHOOL (Major→Elective) ──
        System.out.println("  OG201 'Oil & Gas Fundamentals' is MAJOR for SEG school.");
        System.out.println("  student1 school: " + student1.getSchool());
        System.out.println("  Effective type for student1 (SITE): "
                + oilGas.getEffectiveCourseType(student1));
        // If student1 is SITE, a MAJOR from SEG should show as ELECTIVE
        System.out.println();
        System.out.println("  All papers sorted by citations:");
        resCtrl.printAllUniversityPapers(new ResearchPaperCitationComparator());
        System.out.println();
        System.out.println("  All papers sorted by date:");
        resCtrl.printAllUniversityPapers(new ResearchPaperDateComparator());
        System.out.println();
        System.out.println("  All papers sorted by pages:");
        resCtrl.printAllUniversityPapers(new ResearchPaperPagesComparator());

        // ──────────────────────────────────────────────────────────────────
        printBanner("16. TOP CITED RESEARCHER BY SCHOOL & YEAR");
        resCtrl.printTopCitedResearcherBySchool();
        resCtrl.printTopCitedResearcherOfYear(2026);

        // ──────────────────────────────────────────────────────────────────
        printBanner("17. NON-RESEARCHER TEACHER DEMO");
        // teacher2 is a LECTOR — not a researcher by default
        System.out.println("  teacher2 isResearcher: " + ((Teacher) teacher2).isResearcher());
        ResearchPaper testPaper = new ResearchPaper("Test Paper", "Marat", 5, new Date(), "Test Journal");
        ((Teacher) teacher2).publishPaper(testPaper); // Should print warning
        // Now make teacher2 a researcher explicitly
        ((Teacher) teacher2).setResearcher(true);
        System.out.println("  teacher2 isResearcher after setResearcher(true): " + ((Teacher) teacher2).isResearcher());
        ((Teacher) teacher2).publishPaper(testPaper); // Should succeed now
        // Reset back to non-researcher for CLI demo
        ((Teacher) teacher2).setResearcher(false);
        System.out.println("  teacher2 reset to non-researcher for CLI demo.");

        // ──────────────────────────────────────────────────────────────────
        printBanner("18. SERIALIZATION — SAVE & LOAD DATA");
        db.saveToFile();
        System.out.println("  Data saved. Users count: " + db.getAllUsers().size());
        // Demonstrate loading
        boolean loaded = db.loadFromFile();
        System.out.println("  Data loaded: " + loaded + ". Users count: " + db.getAllUsers().size());

        // ──────────────────────────────────────────────────────────────────
        startCLI(db);
    }

    // ================================================================== //
    //  Interactive CLI
    // ================================================================== //

    private static void startCLI(DataStore db) {
        printBanner("INTERACTIVE CLI MODE");
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║         DEMO LOGIN ACCOUNTS              ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.println("  ║  student1   / pass123   → Student        ║");
        System.out.println("  ║  student2   / pass456   → Student        ║");
        System.out.println("  ║  grad1      / grad123   → GradStudent    ║");
        System.out.println("  ║  teacher1   / teach123  → Teacher (Prof) ║");
        System.out.println("  ║  teacher2   / teach456  → Teacher        ║");
        System.out.println("  ║  manager1   / mgr123    → Manager (OR)   ║");
        System.out.println("  ║  support1   / sup123    → TechSupport    ║");
        System.out.println("  ║  admin1     / admin123  → Admin          ║");
        System.out.println("  ╚══════════════════════════════════════════╝");

        LoginBoundary loginBoundary = new LoginBoundary();
        boolean keepRunning = true;
        while (keepRunning) {
            User activeUser = loginBoundary.showLoginMenu();
            if (activeUser == null) {
                keepRunning = false;
            } else {
                MainMenuBoundary menu = new MainMenuBoundary();
                menu.showMainMenu(activeUser);
                // Auto-save data on every logout
                db.saveToFile();
                System.out.println("\n  Logged out: " + activeUser.getLogin());
                System.out.print("  Login with another account? (y/n): ");
                keepRunning = new java.util.Scanner(System.in).nextLine().trim().equalsIgnoreCase("y");
            }
        }
        // Final save before exit
        db.saveToFile();
        System.out.println("\n  Goodbye!");
    }


    private static void printBanner(String text) {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║  " + text);
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
}

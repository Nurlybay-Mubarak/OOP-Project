package boundary;

import controller.ResearchController;
import enums.CitationFormat;
import model.research.Researcher;
import model.research.ResearchPaper;
import model.research.ResearchProject;
import patterns.ResearchPaperCitationComparator;
import patterns.ResearchPaperDateComparator;
import patterns.ResearchPaperPagesComparator;
import storage.DataStore;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ResearcherBoundary {
    private final Researcher researcher;
    private final ResearchController ctrl;
    private final Scanner scanner;

    public ResearcherBoundary(Researcher researcher) {
        this.researcher = researcher;
        this.ctrl = new ResearchController();
        this.scanner = new Scanner(System.in);
    }

    public void showResearchMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══ RESEARCHER MENU ═════════════╗");
            System.out.println("║  1. Publish a paper            ║");
            System.out.println("║  2. View my papers (by date)   ║");
            System.out.println("║  3. View my papers (by cit.)   ║");
            System.out.println("║  4. Get citation               ║");
            System.out.println("║  5. My H-Index                 ║");
            System.out.println("║  6. Join research project      ║");
            System.out.println("║  7. Top cited researchers      ║");
            System.out.println("║  0. Back                       ║");
            System.out.println("╚════════════════════════════════╝");
            System.out.print("  Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": publishPaper(); break;
                case "2": printPapersByDate(); break;
                case "3": printPapersByCitations(); break;
                case "4": getCitation(); break;
                case "5": System.out.println("  H-Index: " + researcher.calculateHIndex()); break;
                case "6": joinProject(); break;
                case "7": topResearchers(); break;
                case "0": running = false; break;
                default: System.out.println("  Invalid option.");
            }
        }
    }

    public void publishPaper() {
        System.out.print("  Paper title: ");
        String title = scanner.nextLine().trim();
        System.out.print("  Pages: ");
        int pages = Integer.parseInt(scanner.nextLine().trim());
        ResearchPaper paper = new ResearchPaper(title, "", pages, new Date(), "Unknown Journal");
        ctrl.publishPaper(researcher, paper);
        System.out.println("  Paper published: " + title);
    }

    public void printPapersByDate() {
        List<ResearchPaper> sorted = researcher.printPapers(new ResearchPaperDateComparator());
        if (sorted.isEmpty()) { System.out.println("  No papers."); return; }
        sorted.forEach(p -> System.out.println("  " + p));
    }

    public void printPapersByCitations() {
        List<ResearchPaper> sorted = researcher.printPapers(new ResearchPaperCitationComparator());
        sorted.forEach(p -> System.out.println("  " + p));
    }

    public void getCitation() {
        if (researcher.getPapers().isEmpty()) { System.out.println("  No papers."); return; }
        System.out.println("  Select paper (0-based index):");
        List<ResearchPaper> papers = researcher.getPapers();
        for (int i = 0; i < papers.size(); i++) System.out.println("  [" + i + "] " + papers.get(i).getTitle());
        System.out.print("  Index: ");
        int idx = Integer.parseInt(scanner.nextLine().trim());
        if (idx < 0 || idx >= papers.size()) { System.out.println("  Invalid index."); return; }
        System.out.println("  1. PLAIN_TEXT  2. BIBTEX");
        System.out.print("  Format: ");
        String f = scanner.nextLine().trim();
        CitationFormat fmt = f.equals("2") ? CitationFormat.BIBTEX : CitationFormat.PLAIN_TEXT;
        System.out.println(researcher.getCitation(papers.get(idx), fmt));
    }

    public void joinProject() {
        System.out.print("  Project name: ");
        String name = scanner.nextLine().trim();
        ResearchProject project = new ResearchProject(name, "Research project");
        try {
            ctrl.joinProject((model.users.User) researcher, project);
        } catch (exceptions.NotResearcherException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    public void viewProjects() {
        System.out.println("  (Research projects list — managed via ResearchController)");
    }

    public void topResearchers() {
        ctrl.printTopCitedResearchers().forEach(r ->
            System.out.println("  H=" + r.calculateHIndex() + " | " + r));
    }
}
package boundary;

import controller.SupportController;
import enums.RequestStatus;
import model.support.SupportRequest;
import model.users.TechSupportSpecialist;
import storage.DataStore;

import java.util.List;
import java.util.Scanner;

/**
 * Console UI for TechSupportSpecialist — full lifecycle management.
 */
public class TechSupportBoundary {

    private final TechSupportSpecialist specialist;
    private final SupportController     supCtrl;
    private final Scanner               scanner;

    public TechSupportBoundary(TechSupportSpecialist specialist) {
        this.specialist = specialist;
        this.supCtrl    = new SupportController();
        this.scanner    = new Scanner(System.in);
    }

    public void showSupportMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══ TECH SUPPORT MENU ══════════════════╗");
            System.out.println("║  1.  View NEW requests                ║");
            System.out.println("║  2.  View ALL requests                ║");
            System.out.println("║  3.  View request detail              ║");
            System.out.println("║  4.  Accept a request                 ║");
            System.out.println("║  5.  Reject a request                 ║");
            System.out.println("║  6.  Mark as DONE                     ║");
            System.out.println("║  7.  View by status                   ║");
            System.out.println("║  8.  Change language                  ║");
            System.out.println("║  0.  Logout                           ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("  Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewNewRequests();      break;
                case "2": viewAllRequests();      break;
                case "3": viewRequestDetail();    break;
                case "4": acceptRequest();        break;
                case "5": rejectRequest();        break;
                case "6": markDone();             break;
                case "7": viewByStatus();         break;
                case "8": changeLanguage();       break;
                case "0": running = false;        break;
                default:  System.out.println("  Invalid option.");
            }
        }
    }

    // ── 1. View New Requests ─────────────────────────────────────────────
    public void viewNewRequests() {
        List<SupportRequest> reqs = supCtrl.viewNewRequests();
        if (reqs.isEmpty()) { System.out.println("  No new requests."); return; }
        System.out.println("\n  --- NEW Requests (" + reqs.size() + ") ---");
        printRequests(reqs);
    }

    // ── 2. View All Requests ─────────────────────────────────────────────
    public void viewAllRequests() {
        List<SupportRequest> reqs = DataStore.getInstance().getAllSupportRequests();
        if (reqs.isEmpty()) { System.out.println("  No requests at all."); return; }
        System.out.println("\n  --- ALL Requests (" + reqs.size() + ") ---");
        printRequests(reqs);
    }

    // ── 3. View Request Detail ───────────────────────────────────────────
    public void viewRequestDetail() {
        System.out.print("  Request ID prefix (6 chars): ");
        String prefix = scanner.nextLine().trim();
        findRequestByPrefix(prefix).ifPresentOrElse(
            req -> {
                supCtrl.viewRequest(req);   // status → VIEWED
                System.out.println("  ── Request Detail ──────────────────");
                System.out.println("  ID     : " + req.getId());
                System.out.println("  Author : " + req.getAuthor().getLogin());
                System.out.println("  Status : " + req.getStatus());
                System.out.println("  Urgency: " + req.getUrgencyLevel());
                System.out.println("  Created: " + req.getCreatedDate());
                System.out.println("  Desc   : " + req.getDescription());
            },
            () -> System.out.println("  Request not found.")
        );
    }

    // ── 4. Accept Request ────────────────────────────────────────────────
    public void acceptRequest() {
        System.out.print("  Request ID prefix: ");
        String prefix = scanner.nextLine().trim();
        findRequestByPrefix(prefix).ifPresentOrElse(
            req -> { supCtrl.acceptRequest(specialist, req); System.out.println("  ACCEPTED: " + req.getId().substring(0,6)); },
            () -> System.out.println("  Not found.")
        );
    }

    // ── 5. Reject Request ────────────────────────────────────────────────
    public void rejectRequest() {
        System.out.print("  Request ID prefix: ");
        String prefix = scanner.nextLine().trim();
        findRequestByPrefix(prefix).ifPresentOrElse(
            req -> { supCtrl.rejectRequest(specialist, req); System.out.println("  REJECTED: " + req.getId().substring(0,6)); },
            () -> System.out.println("  Not found.")
        );
    }

    // ── 6. Mark as Done ──────────────────────────────────────────────────
    public void markDone() {
        System.out.print("  Request ID prefix: ");
        String prefix = scanner.nextLine().trim();
        findRequestByPrefix(prefix).ifPresentOrElse(
            req -> { supCtrl.markAsDone(specialist, req); System.out.println("  DONE: " + req.getId().substring(0,6)); },
            () -> System.out.println("  Not found.")
        );
    }

    // ── 7. View by Status ─────────────────────────────────────────────────
    public void viewByStatus() {
        System.out.println("  Status: 1=NEW  2=VIEWED  3=ACCEPTED  4=REJECTED  5=DONE");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        RequestStatus status = switch (ch) {
            case "2" -> RequestStatus.VIEWED;
            case "3" -> RequestStatus.ACCEPTED;
            case "4" -> RequestStatus.REJECTED;
            case "5" -> RequestStatus.DONE;
            default  -> RequestStatus.NEW;
        };
        List<SupportRequest> filtered = DataStore.getInstance().getAllSupportRequests()
                .stream().filter(r -> r.getStatus() == status).toList();
        if (filtered.isEmpty()) { System.out.println("  No requests with status: " + status); return; }
        System.out.println("\n  --- " + status + " Requests ---");
        printRequests(filtered);
    }

    // ── 8. Change Language ────────────────────────────────────────────────
    public void changeLanguage() {
        System.out.println("  1. KZ   2. RU   3. EN");
        System.out.print("  Choice: ");
        String ch = scanner.nextLine().trim();
        enums.Language lang = ch.equals("1") ? enums.Language.KZ : ch.equals("2") ? enums.Language.RU : enums.Language.EN;
        specialist.setLanguage(lang);
        System.out.println("  Language set to: " + lang);
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private void printRequests(List<SupportRequest> reqs) {
        reqs.forEach(r -> System.out.printf("  [%s] %-8s | %-8s | %s%n",
                r.getId().substring(0,6), r.getStatus(), r.getUrgencyLevel(), r.getDescription()));
    }

    private java.util.Optional<SupportRequest> findRequestByPrefix(String prefix) {
        return DataStore.getInstance().getAllSupportRequests().stream()
                .filter(r -> r.getId().startsWith(prefix))
                .findFirst();
    }
}
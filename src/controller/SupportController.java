package controller;

import enums.RequestStatus;
import enums.UrgencyLevel;
import model.support.SupportRequest;
import model.users.TechSupportSpecialist;
import model.users.User;
import storage.DataStore;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles technical support request lifecycle:
 * creation, viewing, accepting, rejecting, and closing.
 */
public class SupportController {

    public SupportController() {}

    /**
     * Create a new support request authored by a user.
     *
     * @param author      the user submitting the request
     * @param description a description of the issue
     * @param urgency     the urgency level
     * @return the created SupportRequest
     */
    public SupportRequest createRequest(User author, String description, UrgencyLevel urgency) {
        if (author == null || description == null || description.isBlank()) return null;
        SupportRequest request = new SupportRequest(author, description, urgency);
        DataStore.getInstance().addSupportRequest(request);
        System.out.println("[SUPPORT] Request created by " + author.getLogin()
                + " [" + urgency + "]: " + description);
        return request;
    }

    /**
     * Retrieve all requests with NEW status.
     *
     * @return list of new support requests
     */
    public List<SupportRequest> viewNewRequests() {
        return DataStore.getInstance().getAllSupportRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW)
                .collect(Collectors.toList());
    }

    /**
     * Mark a request as viewed (specialist has read it).
     *
     * @param request the request to mark
     */
    public void viewRequest(SupportRequest request) {
        if (request == null) return;
        request.updateStatus(RequestStatus.VIEWED);
    }

    /**
     * Accept a support request (specialist takes ownership).
     *
     * @param specialist the specialist accepting
     * @param request    the request to accept
     */
    public void acceptRequest(TechSupportSpecialist specialist, SupportRequest request) {
        if (specialist == null || request == null) return;
        specialist.acceptRequest(request);
    }

    /**
     * Reject a support request with an implicit reason logged to console.
     *
     * @param specialist the specialist rejecting
     * @param request    the request to reject
     */
    public void rejectRequest(TechSupportSpecialist specialist, SupportRequest request) {
        if (specialist == null || request == null) return;
        specialist.rejectRequest(request);
    }

    /**
     * Mark a support request as done (issue resolved).
     *
     * @param specialist the specialist closing the request
     * @param request    the request to close
     */
    public void markAsDone(TechSupportSpecialist specialist, SupportRequest request) {
        if (specialist == null || request == null) return;
        specialist.markAsDone(request);
    }

    /**
     * Return all support requests in the system.
     */
    public List<SupportRequest> getAllRequests() {
        return DataStore.getInstance().getAllSupportRequests();
    }
}
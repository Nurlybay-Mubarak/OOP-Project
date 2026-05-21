package model.users;

import enums.RequestStatus;
import model.support.SupportRequest;
import storage.DataStore;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a Technical Support Specialist.
 * Handles SupportRequest objects: views new requests, accepts, rejects, or marks them done.
 */
public class TechSupportSpecialist extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public TechSupportSpecialist() {
        super();
    }

    public TechSupportSpecialist(String login, String password, String firstName,
                                  String lastName, String email, double salary) {
        super(login, password, firstName, lastName, email, salary);
    }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Returns all NEW support requests from the DataStore.
     *
     * @return list of requests with NEW status
     */
    public List<SupportRequest> viewNewRequests() {
        return DataStore.getInstance().getAllSupportRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW)
                .collect(Collectors.toList());
    }

    /**
     * Accept a support request: changes its status from NEW/VIEWED to ACCEPTED.
     *
     * @param r the request to accept
     */
    public void acceptRequest(SupportRequest r) {
        if (r == null) return;
        r.updateStatus(RequestStatus.ACCEPTED);
        System.out.println("[SUPPORT] " + getLogin() + " ACCEPTED request: " + r.getId());
    }

    /**
     * Reject a support request: changes its status to REJECTED.
     *
     * @param r the request to reject
     */
    public void rejectRequest(SupportRequest r) {
        if (r == null) return;
        r.updateStatus(RequestStatus.REJECTED);
        System.out.println("[SUPPORT] " + getLogin() + " REJECTED request: " + r.getId());
    }

    /**
     * Mark a support request as completed (DONE).
     *
     * @param r the request to close
     */
    public void markAsDone(SupportRequest r) {
        if (r == null) return;
        r.updateStatus(RequestStatus.DONE);
        System.out.println("[SUPPORT] " + getLogin() + " marked DONE: " + r.getId());
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() { return super.hashCode(); }

    @Override
    public String toString() {
        return "TechSupportSpecialist{login='" + getLogin() + "', name='"
                + getFirstName() + " " + getLastName() + "'}";
    }
}
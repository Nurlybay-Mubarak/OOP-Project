package model.users;                              

import model.support.SupportRequest;             
import enums.RequestStatus;                       
import storage.DataStore;                         

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Tech support specialists can view, accept, reject requests
 * and mark them as done.
 * Statuses: NEW → VIEWED → ACCEPTED/REJECTED → DONE
 */
public class TechSupportSpecialist extends Employee {

    private List<SupportRequest> requests;         

    /**
     * Default constructor
     */
    public TechSupportSpecialist() {
        super();
        this.requests = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     */
    public TechSupportSpecialist(String id, String login, String password,
                                  String firstName, String lastName, String email,
                                  String employeeId, double salary) {
        super(id, login, password, firstName, lastName, email, employeeId, salary);
        this.requests = new ArrayList<>();
    }

    /**
     * View new requests. When seen, status changes to VIEWED.
     */
    public List<SupportRequest> viewNewRequests() {
        List<SupportRequest> allRequests = DataStore.getInstance().getRequests();
        List<SupportRequest> newRequests = new ArrayList<>();

        for (SupportRequest r : allRequests) {
            if (r.getStatus() == RequestStatus.NEW) {
                r.setStatus(RequestStatus.VIEWED);   
                newRequests.add(r);
                System.out.println(r);
            }
        }

        if (newRequests.isEmpty()) {
            System.out.println("No new requests.");
        }

        return newRequests;
    }

    /**
     * Accept a request
     */
    public void acceptRequest(SupportRequest r) {
        r.setStatus(RequestStatus.ACCEPTED);
        System.out.println("Request accepted: " + r.getDescription());
    }

    /**
     * Reject a request
     */
    public void rejectRequest(SupportRequest r) {
        r.setStatus(RequestStatus.REJECTED);
        System.out.println("Request rejected: " + r.getDescription());
    }

    /**
     * Mark request as done
     */
    public void markAsDone(SupportRequest r) {
        r.setStatus(RequestStatus.DONE);
        System.out.println("Request completed: " + r.getDescription());
    }

    // GETTERS & SETTERS

    public List<SupportRequest> getRequests() { return requests; }
    public void setRequests(List<SupportRequest> requests) { this.requests = requests; }

    @Override
    public String toString() {
        return "Tech Support: " + getFirstName() + " " + getLastName();
    }
}
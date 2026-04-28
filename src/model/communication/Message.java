package model.communication;

import model.users.Employee;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a message between employees
 */
public class Message implements Serializable {

    private Employee sender;
    private Employee receiver;
    private String text;
    private LocalDateTime date;
    private boolean isOfficial;

    /**
     * Constructor
     */
    public Message(Employee sender, Employee receiver, String text, boolean isOfficial) {
        if (sender == null || receiver == null || text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Invalid message data");
        }

        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.isOfficial = isOfficial;
        this.date = LocalDateTime.now();
    }

    // GETTERS

    public Employee getSender() { return sender; }
    public Employee getReceiver() { return receiver; }
    public String getText() { return text; }
    public LocalDateTime getDate() { return date; }
    public boolean isOfficial() { return isOfficial; }

    @Override
    public String toString() {
        return "[" + date + "] "
                + sender.getFirstName() + " → "
                + receiver.getFirstName()
                + ": " + text
                + (isOfficial ? " (OFFICIAL)" : "");
    }
}
package model.communication;

import model.users.Employee;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Represents a direct message sent between two Employee users.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private Employee sender;
    private Employee receiver;
    private String   text;
    private Date     date;
    private boolean  isOfficial;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Message() {
        this.date       = new Date();
        this.isOfficial = false;
    }

    public Message(Employee sender, Employee receiver, String text, boolean isOfficial) {
        this.sender     = sender;
        this.receiver   = receiver;
        this.text       = text;
        this.isOfficial = isOfficial;
        this.date       = new Date();
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public Employee getSender()            { return sender; }
    public void     setSender(Employee s)  { this.sender = s; }

    public Employee getReceiver()              { return receiver; }
    public void     setReceiver(Employee r)    { this.receiver = r; }

    public String getText()           { return text; }
    public void   setText(String t)   { this.text = t; }

    public Date getDate()           { return date; }
    public void setDate(Date d)     { this.date = d; }

    public boolean isOfficial()              { return isOfficial; }
    public void    setOfficial(boolean flag) { this.isOfficial = flag; }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(sender, message.sender)
                && Objects.equals(receiver, message.receiver)
                && Objects.equals(text, message.text)
                && Objects.equals(date, message.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, text, date);
    }

    @Override
    public String toString() {
        return "Message{from=" + (sender != null ? sender.getLogin() : "?")
                + ", to=" + (receiver != null ? receiver.getLogin() : "?")
                + ", official=" + isOfficial + ", text='" + text + "'}";
    }
}
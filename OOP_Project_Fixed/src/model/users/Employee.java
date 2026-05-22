package model.users;

import model.communication.Message;
import storage.DataStore;

import java.io.Serializable;
import java.util.Date;

/**
 * Abstract class representing any university employee (Teacher, Manager, Admin, TechSupportSpecialist).
 * Extends User and adds employment-specific attributes.
 */
public abstract class Employee extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    private double salary;
    private Date   hireDate;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Employee() {
        super();
        this.hireDate = new Date();
    }

    public Employee(String login, String password, String firstName,
                    String lastName, String email, double salary) {
        super(login, password, firstName, lastName, email);
        this.salary   = salary;
        this.hireDate = new Date();
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public double getSalary()           { return salary; }
    public void   setSalary(double s)   { this.salary = s; }

    public Date getHireDate()           { return hireDate; }
    public void setHireDate(Date date)  { this.hireDate = date; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Returns the employee's current salary. Access-controlled — only
     * the employee themselves (or Admin) should call this.
     *
     * @return salary value
     */
    public double viewSalary() {
        return this.salary;
    }

    /**
     * Send a direct message to another employee.
     * The message is stored in the DataStore's message list.
     *
     * @param receiver the recipient Employee
     * @param text     the message body
     */
    public void sendMessage(Employee receiver, String text) {
        if (receiver == null || text == null || text.isBlank()) return;
        Message msg = new Message(this, receiver, text, false);
        DataStore.getInstance().addMessage(msg);
        System.out.println("[MESSAGE] " + getLogin() + " → " + receiver.getLogin() + ": " + text);
    }
}
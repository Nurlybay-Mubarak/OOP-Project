package model.users;                         

import model.communication.Message;          
import storage.DataStore;                   

import java.io.*;
import java.util.*;

/**
 * Abstract class representing an employee of the university.
 * Extends User. Teacher, Admin, Manager, TechSupportSpecialist inherit from this.
 */
public abstract class Employee extends User {

    private double salary;
    private Date hireDate;
    private String employeeId;

    /**
     * Default constructor
     */
    public Employee() {
        super();
    }

    /**
     * Constructor with parameters
     */
    public Employee(String id, String login, String password,
                    String firstName, String lastName, String email,
                    String employeeId, double salary) {
        super(id, login, password, firstName, lastName, email);
        this.employeeId = employeeId;
        this.salary = salary;
        this.hireDate = new Date();        
    }

    /**
     * View salary of this employee
     */
    public double viewSalary() {
        return this.salary;
    }

    /**
     * Send message to another employee
     * По заданию: "any employee can send the message to any employee"
     */
    public void sendMessage(Employee receiver, String text) {
        Message msg = new Message(this, receiver, text);
        System.out.println("Message sent from " + getFirstName()
                + " to " + receiver.getFirstName() + ": " + text);
    }

    // GETTERS & SETTERS

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName()
                + " (Employee ID: " + employeeId + ")";
    }
}
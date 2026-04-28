package users;

import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Employee extends User {

    /**
     * Default constructor
     */
    public Employee() {
    }

    /**
     * 
     */
    private double salary;

    /**
     * 
     */
    private Date hireDate;

    /**
     * @return
     */
    public double viewSalary() {
        // TODO implement here
        return 0.0d;
    }

    /**
     * @param receiver 
     * @param text
     */
    public void sendMessage(Employee receiver, String text) {
        // TODO implement here
    }

}
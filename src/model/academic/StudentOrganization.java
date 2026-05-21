package model.academic;

import model.users.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a student organisation (club, society, etc.) at the university.
 * Has a head (president) and a list of members.
 */
public class StudentOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    private String        name;
    private Student       head;
    private List<Student> members;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public StudentOrganization() {
        this.members = new ArrayList<>();
    }

    public StudentOrganization(String name, Student head) {
        this.name    = name;
        this.head    = head;
        this.members = new ArrayList<>();
        if (head != null) {
            this.members.add(head);
        }
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public String getName()              { return name; }
    public void   setName(String name)   { this.name = name; }

    public Student getHead()             { return head; }
    public void    setHead(Student h)    { this.head = h; }

    public List<Student> getMembers()    { return members; }
    public void          setMembers(List<Student> m) { this.members = m; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Add a student member (no duplicates).
     *
     * @param s the student to add
     */
    public void addMember(Student s) {
        if (s != null && !members.contains(s)) {
            members.add(s);
        }
    }

    /**
     * Remove a member from the organisation.
     *
     * @param s the student to remove
     */
    public void removeMember(Student s) {
        members.remove(s);
    }

    /**
     * Returns true if the student is the head (president) of this organisation.
     *
     * @param s the student to check
     * @return true if head
     */
    public boolean isHead(Student s) {
        return head != null && head.equals(s);
    }

    /**
     * Promote a member to head (president) of the organisation.
     * The student must already be a member.
     *
     * @param s the student to promote
     */
    public void promoteToHead(Student s) {
        if (s == null) return;
        if (!members.contains(s)) addMember(s);
        this.head = s;
        System.out.println("[ORG] " + s.getLogin() + " is now head of " + name);
    }

    /**
     * Returns true if the student is a member of this organisation.
     */
    public boolean isMember(Student s) {
        return members.contains(s);
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentOrganization that = (StudentOrganization) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "StudentOrganization{name='" + name + "', members=" + members.size()
                + ", head=" + (head != null ? head.getLastName() : "none") + "}";
    }
}
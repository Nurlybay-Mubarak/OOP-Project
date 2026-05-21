package model.academic;

import enums.LessonType;
import model.users.Teacher;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a single lesson (lecture, practice, or lab) within a Course.
 */
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    private LessonType type;
    private String     room;
    private String     dayOfWeek;
    private String     startTime;
    private int        duration;   // in minutes
    private Teacher    teacher;

    // ------------------------------------------------------------------ //
    //  Constructors
    // ------------------------------------------------------------------ //

    public Lesson() {
    }

    public Lesson(LessonType type, String room, String dayOfWeek,
                  String startTime, int duration, Teacher teacher) {
        this.type       = type;
        this.room       = room;
        this.dayOfWeek  = dayOfWeek;
        this.startTime  = startTime;
        this.duration   = duration;
        this.teacher    = teacher;
    }

    // ------------------------------------------------------------------ //
    //  Getters & Setters
    // ------------------------------------------------------------------ //

    public LessonType getType()             { return type; }
    public void       setType(LessonType t) { this.type = t; }

    public String getRoom()            { return room; }
    public void   setRoom(String r)    { this.room = r; }

    public String getDayOfWeek()           { return dayOfWeek; }
    public void   setDayOfWeek(String d)   { this.dayOfWeek = d; }

    public String getStartTime()             { return startTime; }
    public void   setStartTime(String time)  { this.startTime = time; }

    public int  getDuration()      { return duration; }
    public void setDuration(int d) { this.duration = d; }

    public Teacher getTeacher()             { return teacher; }
    public void    setTeacher(Teacher t)    { this.teacher = t; }

    // ------------------------------------------------------------------ //
    //  Business Methods
    // ------------------------------------------------------------------ //

    /**
     * Returns a human-readable schedule info string.
     */
    public String getInfo() {
        return type + " | Room: " + room + " | " + dayOfWeek + " " + startTime
                + " (" + duration + " min)"
                + (teacher != null ? " | Teacher: " + teacher.getLastName() : "");
    }

    // ------------------------------------------------------------------ //
    //  Standard Overrides
    // ------------------------------------------------------------------ //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return duration == lesson.duration
                && type == lesson.type
                && Objects.equals(room, lesson.room)
                && Objects.equals(dayOfWeek, lesson.dayOfWeek)
                && Objects.equals(startTime, lesson.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, room, dayOfWeek, startTime, duration);
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
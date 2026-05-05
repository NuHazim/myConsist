package com.example.myconsist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Habit {
    @PrimaryKey(autoGenerate = true)
    private long habitId;
    private String title;
    private String daysOfWeek;
    private int position;

    public Habit(String title, String daysOfWeek, int position) {
        this.title = title;
        this.daysOfWeek = daysOfWeek;
        this.position = position;
    }

    public long getHabitId() {
        return habitId;
    }

    public void setHabitId(long habitId) {
        this.habitId = habitId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "habitId=" + habitId +
                ", title='" + title + '\'' +
                ", daysOfWeek='" + daysOfWeek + '\'' +
                ", position=" + position +
                '}';
    }
}

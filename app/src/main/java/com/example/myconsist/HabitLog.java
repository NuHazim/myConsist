package com.example.myconsist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HabitLog {
    @PrimaryKey(autoGenerate = true)
    private long habitLogId;
    private long habitId;
    private String logDate;
    private int isDone;

    public HabitLog(long habitId, String logDate, int isDone) {
        this.habitId = habitId;
        this.logDate = logDate;
        this.isDone = isDone;
    }

    public long getHabitLogId() {
        return habitLogId;
    }

    public void setHabitLogId(long habitLogId) {
        this.habitLogId = habitLogId;
    }

    public long getHabitId() {
        return habitId;
    }

    public void setHabitId(long habitId) {
        this.habitId = habitId;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    @Override
    public String toString() {
        return "HabitLog{" +
                "habitLogId=" + habitLogId +
                ", habitId=" + habitId +
                ", logDate='" + logDate + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}

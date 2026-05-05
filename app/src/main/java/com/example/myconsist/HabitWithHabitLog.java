package com.example.myconsist;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class HabitWithHabitLog {
    @Embedded
    public Habit habit;
    @Relation(
            parentColumn = "habitId",
            entityColumn = "habitId"
    )
    public List<HabitLog> logs;

    public HabitWithHabitLog() {
    }
}

package com.example.myconsist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface appDao {
    //TASK GROUP DAO
    @Insert
    long insert(TaskGroup taskGroup);

    @Update
    void update(TaskGroup taskGroup);

    @Delete
    void delete(TaskGroup taskGroup);

    @Query("SELECT * FROM TaskGroup ORDER BY position ASC")
    LiveData<List<TaskGroup>> getAllGroups();

    @Transaction
    @Query("SELECT * FROM TaskGroup ORDER BY position ASC")
    LiveData<List<TaskGroupWithTask>> getAllGroupsWithTasks();

    //TASK DAO

    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM Task WHERE groupId = :groupId ORDER BY position ASC")
    LiveData<List<Task>> getTasksByGroup(long groupId);

    // for the reminder page - pending tasks with deadlines, most urgent first
    @Query("SELECT * FROM Task WHERE deadlineMs IS NOT NULL AND isDone = 0 ORDER BY deadlineMs ASC")
    LiveData<List<Task>> getUpcomingTasks();

    //HABIT DAO
    @Insert
    long insert(Habit habit);

    @Update
    void update(Habit habit);

    @Delete
    void delete(Habit habit);

    @Query("SELECT * FROM Habit ORDER BY position ASC")
    LiveData<List<Habit>> getAllHabits();

    @Transaction
    @Query("SELECT * FROM Habit ORDER BY position ASC")
    LiveData<List<HabitWithHabitLog>> getAllHabitsWithLogs();

    //HABIT LOG DAO

    @Insert
    long insert(HabitLog habitLog);

    @Update
    void update(HabitLog habitLog);

    // get a specific log entry for a habit on a specific date
    @Query("SELECT * FROM HabitLog WHERE habitId = :habitId AND logDate = :date LIMIT 1")
    HabitLog getLog(long habitId, String date);

    // delete all logs for a habit (used when user deletes the habit)
    @Query("DELETE FROM HabitLog WHERE habitId = :habitId")
    void deleteLogsForHabit(long habitId);
}

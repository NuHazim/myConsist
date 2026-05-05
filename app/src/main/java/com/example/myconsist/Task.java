package com.example.myconsist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private long taskId;

    private long groupId;

    private String title;
    private int isDone;

    private long deadlineMs;

    private int position;

    public Task(long groupId,String title, int isDone, long deadlineMs, int position) {
        this.groupId=groupId;
        this.title = title;
        this.isDone = isDone;
        this.deadlineMs = deadlineMs;
        this.position = position;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public long getDeadlineMs() {
        return deadlineMs;
    }

    public void setDeadlineMs(long deadlineMs) {
        this.deadlineMs = deadlineMs;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", groupId=" + groupId +
                ", title='" + title + '\'' +
                ", isDone=" + isDone +
                ", deadlineMs=" + deadlineMs +
                ", position=" + position +
                '}';
    }
}

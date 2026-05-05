package com.example.myconsist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TaskGroup {
    @PrimaryKey(autoGenerate = true)
    private long groupId;
    private String groupName;
    private int position;

    @Override
    public String toString() {
        return "TaskGroup{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", position=" + position +
                '}';
    }

    public TaskGroup(String groupName, int position) {
        this.groupName = groupName;
        this.position=position;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

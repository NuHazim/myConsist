package com.example.myconsist;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TaskGroupWithTask {
    @Embedded
    public TaskGroup taskGroup;

    @Relation(
            parentColumn = "groupId",
            entityColumn = "groupId"
    )
    public List<Task> tasks;
    public TaskGroupWithTask(){

    }
}

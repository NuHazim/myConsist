package com.example.myconsist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ReminderFragment extends Fragment {
    private appDatabase db;
    private appDao dao;
    private LinearLayout noRemindersBox;
    private TextView numReminders;
    private LinearLayout remindersBox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_reminder, container, false);
        db=appDatabase.getInstance(requireContext());
        dao=db.appDao();
        noRemindersBox=view.findViewById(R.id.noRemindersBox);
        numReminders=view.findViewById(R.id.numReminders);
        dao.getAllGroupsWithTasks().observe(getViewLifecycleOwner(), new Observer<List<TaskGroupWithTask>>() {
            @Override
            public void onChanged(List<TaskGroupWithTask> groupsWithTasks) {
                renderReminders(groupsWithTasks);
            }
        });
        return view;
    }
    public void renderReminders(List<TaskGroupWithTask> groupsWithTasks){
        int countReminders=0;
        for (TaskGroupWithTask groupWithTask : groupsWithTasks) {
            TaskGroup group=groupWithTask.taskGroup;
            List<Task> tasks = groupWithTask.tasks;
            remindersBox.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (tasks != null) {
                countReminders += tasks.size();
                for(Task task: tasks){
                    if(task.getDeadlineMs()>0){

                    }
                }
            }


        }
        numReminders.setText(String.valueOf(countReminders));
        if (countReminders == 0) {
            noRemindersBox.setVisibility(View.VISIBLE);
        } else {
            noRemindersBox.setVisibility(View.GONE);
        }


    }
}
package com.example.myconsist;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderFragment extends Fragment {
    private appDatabase db;
    private appDao dao;
    private LinearLayout noRemindersBox;
    private TextView numReminders;
    private LinearLayout remindersBox;

    // Helper class to hold task and its group name for flat sorting
    private static class ReminderItem {
        Task task;
        String groupName;

        ReminderItem(Task task, String groupName) {
            this.task = task;
            this.groupName = groupName;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        db = appDatabase.getInstance(requireContext());
        dao = db.appDao();
        noRemindersBox = view.findViewById(R.id.noRemindersBox);
        numReminders = view.findViewById(R.id.numReminders);
        remindersBox = view.findViewById(R.id.remindersBox);
        
        dao.getAllGroupsWithTasks().observe(getViewLifecycleOwner(), new Observer<List<TaskGroupWithTask>>() {
            @Override
            public void onChanged(List<TaskGroupWithTask> groupsWithTasks) {
                renderReminders(groupsWithTasks);
            }
        });
        return view;
    }

    public void renderReminders(List<TaskGroupWithTask> groupsWithTasks) {
        remindersBox.removeAllViews();
        
        // 1. Flatten all tasks with deadlines into a single list for global sorting
        List<ReminderItem> allReminders = new ArrayList<>();
        for (TaskGroupWithTask groupWithTask : groupsWithTasks) {
            if (groupWithTask.tasks != null) {
                for (Task task : groupWithTask.tasks) {
                    if (task.getDeadlineMs() > 0) {
                        allReminders.add(new ReminderItem(task, groupWithTask.taskGroup.getGroupName()));
                    }
                }
            }
        }

        // 2. Sort by deadline: Soonest (most urgent) on top
        Collections.sort(allReminders, new Comparator<ReminderItem>() {
            @Override
            public int compare(ReminderItem o1, ReminderItem o2) {
                return Long.compare(o1.task.getDeadlineMs(), o2.task.getDeadlineMs());
            }
        });

        long now = System.currentTimeMillis();
        
        // Calculate Tomorrow boundaries
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startOfTomorrow = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        long endOfTomorrow = cal.getTimeInMillis();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        SimpleDateFormat dateFmt = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        for (ReminderItem item : allReminders) {
            Task task = item.task;
            View reminderView;
            boolean isDone = task.getIsDone() == 1;
            long deadline = task.getDeadlineMs();

            // Select correct box type
            if (isDone) {
                reminderView = inflater.inflate(R.layout.completed_reminder_box, remindersBox, false);
            } else if (deadline < now) {
                reminderView = inflater.inflate(R.layout.overdue_reminder_box, remindersBox, false);
                
                // Calculate days overdue precisely
                long diff = now - deadline;
                long days = diff / (1000 * 60 * 60 * 24);
                TextView statusText = reminderView.findViewById(R.id.statusText);
                if (statusText != null) {
                    if (days > 0) {
                        statusText.setText(days + (days == 1 ? " day overdue" : " days overdue"));
                    } else {
                        statusText.setText("Overdue today");
                    }
                }
            } else if (deadline >= startOfTomorrow && deadline < endOfTomorrow) {
                reminderView = inflater.inflate(R.layout.tomorrow_reminder_box, remindersBox, false);
            } else {
                reminderView = inflater.inflate(R.layout.later_reminder_box, remindersBox, false);
            }

            // Fill common views
            TextView groupTv = reminderView.findViewById(R.id.remindersGroup);
            TextView textTv = reminderView.findViewById(R.id.reminderText);
            TextView dateTv = reminderView.findViewById(R.id.dateReminder);
            TextView timeTv = reminderView.findViewById(R.id.timeReminder);

            if (groupTv != null) groupTv.setText(item.groupName);
            if (textTv != null) textTv.setText(task.getTitle());
            if (dateTv != null) dateTv.setText(dateFmt.format(new Date(deadline)));
            if (timeTv != null) timeTv.setText(timeFmt.format(new Date(deadline)));

            // Add click listener to navigate to TodoList and open the group
            reminderView.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToTodoList(task.getGroupId());
                }
            });

            remindersBox.addView(reminderView);
        }

        numReminders.setText(String.valueOf(allReminders.size()));
        noRemindersBox.setVisibility(allReminders.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
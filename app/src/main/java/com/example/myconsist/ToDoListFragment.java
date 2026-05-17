package com.example.myconsist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ToDoListFragment extends Fragment {

    private appDatabase db;
    private appDao dao;
    private LinearLayout listBox;
    private EditText groupInput;
    private Button addGroupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        // Initialize Database
        db = appDatabase.getInstance(requireContext());
        dao = db.appDao();

        // Initialize Views
        listBox = view.findViewById(R.id.listBox);
        groupInput = view.findViewById(R.id.groupInput);
        addGroupButton = view.findViewById(R.id.addGroupButton);

        // Add Group Button Logic
        addGroupButton.setOnClickListener(v -> {
            String groupName = groupInput.getText().toString().trim();
            if (!groupName.isEmpty()) {
                int position = 0; 
                TaskGroup newGroup = new TaskGroup(groupName, position);
                dao.insert(newGroup);
                groupInput.setText("");
            } else {
                Toast.makeText(getContext(), "Please enter a group name", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe Database Changes
        dao.getAllGroupsWithTasks().observe(getViewLifecycleOwner(), new Observer<List<TaskGroupWithTask>>() {
            @Override
            public void onChanged(List<TaskGroupWithTask> groupsWithTasks) {
                renderList(groupsWithTasks);
            }
        });

        return view;
    }

    private void renderList(List<TaskGroupWithTask> groupsWithTasks) {
        listBox.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (TaskGroupWithTask groupWithTask : groupsWithTasks) {
            TaskGroup group = groupWithTask.taskGroup;
            List<Task> tasks = groupWithTask.tasks;

            // Inflate Group Layout
            View groupView = inflater.inflate(R.layout.thegroup, listBox, false);
            
            TextView groupNameTv = groupView.findViewById(R.id.groupName);
            TextView delGroupBtn = groupView.findViewById(R.id.delButton);
            LinearLayout groupDetails = groupView.findViewById(R.id.theGroupDetails);
            LinearLayout itemsList = groupView.findViewById(R.id.theList);
            EditText taskInput = groupView.findViewById(R.id.taskInput);
            EditText dateInput = groupView.findViewById(R.id.dateInput);
            Button addTaskBtn = groupView.findViewById(R.id.addTaskButton);

            groupNameTv.setText(group.getGroupName());

            // Date Picker logic for this specific group
            dateInput.setFocusable(false);
            dateInput.setClickable(true);
            dateInput.setOnClickListener(v -> {
                final Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    dateInput.setText(sdf.format(calendar.getTime()));
                    dateInput.setTag(calendar.getTimeInMillis());
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            });

            // Toggle Expand/Collapse
            groupNameTv.setOnClickListener(v -> {
                if (groupDetails.getVisibility() == View.VISIBLE) {
                    groupDetails.setVisibility(View.GONE);
                } else {
                    groupDetails.setVisibility(View.VISIBLE);
                }
            });

            // Delete Group
            delGroupBtn.setOnClickListener(v -> {
                dao.delete(group);
            });

            // Add Task Logic
            addTaskBtn.setOnClickListener(v -> {
                String taskTitle = taskInput.getText().toString().trim();
                if (!taskTitle.isEmpty()) {
                    Long selectedMs = (Long) dateInput.getTag();
                    long deadlineMs = (selectedMs != null) ? selectedMs : 0L;
                    int pos = tasks != null ? tasks.size() : 0;
                    
                    Task newTask = new Task(group.getGroupId(), taskTitle, 0, deadlineMs, pos);
                    dao.insert(newTask);
                    
                    taskInput.setText("");
                    dateInput.setText("");
                    dateInput.setTag(null);
                }
            });

            // Render Tasks for this Group
            if (tasks != null) {
                for (Task task : tasks) {
                    View itemView = inflater.inflate(R.layout.theitem, itemsList, false);
                    
                    CheckBox checkBox = itemView.findViewById(R.id.checkboxItem);
                    TextView taskName = itemView.findViewById(R.id.nameItem);
                    TextView taskDate = itemView.findViewById(R.id.dateItem);
                    TextView delItemBtn = itemView.findViewById(R.id.delItemButton);

                    taskName.setText(task.getTitle());
                    checkBox.setChecked(task.getIsDone() == 1);
                    
                    // Display date with year if it exists
                    if (task.getDeadlineMs() > 0) {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                        taskDate.setText(sdf.format(new Date(task.getDeadlineMs())));
                        taskDate.setVisibility(View.VISIBLE);
                    } else {
                        taskDate.setVisibility(View.GONE);
                    }

                    // Checkbox Logic
                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        task.setIsDone(isChecked ? 1 : 0);
                        dao.update(task);
                    });

                    // Delete Task
                    delItemBtn.setOnClickListener(v -> {
                        dao.delete(task);
                    });

                    itemsList.addView(itemView);
                }
            }

            listBox.addView(groupView);
        }
    }
}

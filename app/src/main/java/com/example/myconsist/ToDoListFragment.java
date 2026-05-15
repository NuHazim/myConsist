package com.example.myconsist;

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

import java.util.List;

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
                // For position, we'll just use 0 or count for now
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
                // Note: You might want to delete tasks associated with this group too
                // But if your DB has Cascade Delete, Room handles it.
                dao.delete(group);
            });

            // Add Task Logic
            addTaskBtn.setOnClickListener(v -> {
                String taskTitle = taskInput.getText().toString().trim();
                if (!taskTitle.isEmpty()) {
                    // Simple position logic
                    int pos = tasks != null ? tasks.size() : 0;
                    // For now, setting deadline to 0. You can add a DatePicker later.
                    Task newTask = new Task(group.getGroupId(), taskTitle, 0, 0, pos);
                    dao.insert(newTask);
                    taskInput.setText("");
                    dateInput.setText("");
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
                    
                    // Display date if it exists (placeholder logic)
                    if (task.getDeadlineMs() > 0) {
                        taskDate.setText("Due: " + task.getDeadlineMs()); 
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

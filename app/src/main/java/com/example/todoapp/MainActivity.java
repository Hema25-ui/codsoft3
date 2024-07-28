package com.example.todoapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this, this);
        recyclerView.setAdapter(taskAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTaskDialog(null, -1);
            }
        });

        loadTasks(); // Load tasks from local storage
    }

    private void showTaskDialog(Task task, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_task, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.titleInput);
        EditText descriptionInput = dialogView.findViewById(R.id.descriptionInput);

        if (task != null) {
            titleInput.setText(task.getTitle());
            descriptionInput.setText(task.getDescription());
        }

        builder.setTitle(task == null ? "Add Task" : "Edit Task")
                .setPositiveButton(task == null ? "Add" : "Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = titleInput.getText().toString().trim();
                        String description = descriptionInput.getText().toString().trim();
                        if (!title.isEmpty()) {
                            if (task == null) {
                                addTask(new Task(title, description, false));
                            } else {
                                updateTask(position, title, description);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void addTask(Task task) {
        taskList.add(task);
        taskAdapter.notifyDataSetChanged();
        saveTasks(); // Save tasks to local storage
    }

    private void updateTask(int position, String title, String description) {
        Task task = taskList.get(position);
        task.setTitle(title);
        task.setDescription(description);
        taskAdapter.notifyItemChanged(position);
        saveTasks(); // Save tasks to local storage
    }

    @Override
    public void onTaskUpdated(Task task) {
        saveTasks(); // Save tasks to local storage
    }

    @Override
    public void onTaskEditRequested(Task task, int position) {
        showTaskDialog(task, position);
    }

    @Override
    public void onTaskDeleted(Task task, int position) {
        taskList.remove(position);
        taskAdapter.notifyItemRemoved(position);
        saveTasks(); // Save tasks to local storage
    }

    private void loadTasks() {
        // Implement local data loading logic here
    }

    private void saveTasks() {
        // Implement local data saving logic here
    }
}

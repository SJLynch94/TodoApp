package com.slynch.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private EditText taskTitle;
    private EditText taskDescription;
    private DatePicker completionDate;
    private Switch isCompleted;
    private ImageButton updateItemButton;
    private ImageButton deleteItemButton;

    private static final String TAG = "DetailsActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_todo_list:
                    //mAppTitle.setText(R.string.navigation_todo_list);
                    return true;
                case R.id.navigation_todo_details:
                    //mAppTitle.setText(R.string.navigation_todo_details);
                    return true;
            }
            return false;
        }
    };

    int datePickerTimeToTimeStamp(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return (int)(calendar.getTimeInMillis() / 1000L);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);
        Log.d(TAG, "onCreate: Started.");

        taskTitle = (EditText)findViewById(R.id.taskTitleEditText);
        taskDescription = (EditText)findViewById(R.id.taskDescriptionEditText);
        completionDate = (DatePicker)findViewById(R.id.completionDatePicker);
        isCompleted = (Switch) findViewById(R.id.isCompletedSwitch);
        updateItemButton = (ImageButton)findViewById(R.id.addTodoItemButton);
        deleteItemButton = (ImageButton)findViewById(R.id.deleteTodoItemButton);

        TodoItem item = (TodoItem) getIntent().getSerializableExtra("todoItemClicked");

        if (item != null) {
            taskTitle.setText(item.getTaskTitle());
            taskDescription.setText(item.getTaskDescription());
            isCompleted.setChecked(item.getIsCompleted() == 1 ? true : false);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((long) item.getCompletionDate()) * 1000L);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(calendar.DAY_OF_MONTH);
            Log.d(TAG, "Date in UNIX: " + item.getCompletionDate());
            completionDate.init(year, month, day, null);
        } else {
            isCompleted.setVisibility(View.GONE);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_todo_list:
                        Intent intentTodoList = new Intent(DetailsActivity.this, MainActivity.class);
                        startActivity(intentTodoList);
                        finish();
                        break;

                    case R.id.navigation_todo_details:
                        finish();
                        startActivity(getIntent());
                        break;
                }
                return false;
            }
        });

        isCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemTaskTitle = taskTitle.getText().toString();
                String itemTaskDescription = taskDescription.getText().toString();
                int itemIsCompleted = isCompleted.isChecked() == true ? 1 : 0;
                int itemCompletionDate = datePickerTimeToTimeStamp(completionDate.getYear(), completionDate.getMonth(), completionDate.getDayOfMonth(), 0, 0, 0);

                if(MainActivity.getDB().onHasDataInTable(item.getTaskID(), item.getTaskTitle(), item.getTaskDescription(), item.getCompletionDate(), itemIsCompleted)) {
                    Boolean hasUpdatedItem = MainActivity.getDB().onUpdateData(item.getTaskID(), item.getTaskTitle(), item.getTaskDescription(), item.getCompletionDate(), itemIsCompleted);
                    if(hasUpdatedItem) {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " updated.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " has not updated.", Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
            }
        });

        updateItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemTaskTitle = taskTitle.getText().toString();
                String itemTaskDescription = taskDescription.getText().toString();
                int itemIsCompleted = isCompleted.isChecked() == true ? 1 : 0;
                int itemCompletionDate = datePickerTimeToTimeStamp(completionDate.getYear(), completionDate.getMonth(), completionDate.getDayOfMonth(), 0, 0, 0);
                if(item != null) {
                    Boolean hasUpdatedItem = MainActivity.getDB().onUpdateData(item.getTaskID(), itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted);
                    if(hasUpdatedItem) {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " updated.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " has not updated.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Boolean hasInsertedItem = MainActivity.getDB().onInsertData(itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted);
                    if(hasInsertedItem) {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " inserted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " has not been inserted.", Toast.LENGTH_SHORT).show();
                    }
                }
                /*if(MainActivity.getDB().onHasDataInTable(item.getTaskID() == -1 ? 0 : item.getTaskID(), itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted)) {
                    Boolean hasUpdatedItem = MainActivity.getDB().onUpdateData(item.getTaskID(), itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted);
                    if(hasUpdatedItem) {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " updated.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " has not updated.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Boolean hasInsertedItem = MainActivity.getDB().onInsertData(itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted);
                    if(hasInsertedItem) {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " inserted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " has not been inserted.", Toast.LENGTH_SHORT).show();
                    }
                }*/
                finish();
            }
        });

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String itemToDelete = taskTitle.getText().toString();
                Boolean hasDeletedItem = MainActivity.getDB().onDeleteData(item.getTaskID());
                if(hasDeletedItem) {
                    Toast.makeText(DetailsActivity.this, "Entry " + item.getTaskTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, "Entry " + item.getTaskTitle() + " has not deleted.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }
}

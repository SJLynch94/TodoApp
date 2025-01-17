package com.slynch.todoapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    // Member variables to be used within the activity for finding the UI/widgets
    private EditText taskTitle;
    private EditText taskDescription;
    private DatePicker completionDate;
    private TimePicker completionTime;
    private Switch isCompleted;
    private ImageButton updateItemButton;
    private ImageButton deleteItemButton;

    private TodoItemAdapter mAdapter;

    private static final String TAG = "DetailsActivity";

    // On Click Listener for the bottom navigation menu
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_todo_list:
                case R.id.navigation_todo_details:
                    return true;
            }
            return false;
        }
    };

    // Convert date picker to UNIX time stamp
    int datePickerTimeToTimeStamp(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return (int)(calendar.getTimeInMillis() / 1000L);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);
        Log.d(TAG, "onCreate: Started.");

        // Find all UI/widgets of the Details Activity
        taskTitle = (EditText)findViewById(R.id.taskTitleEditText);
        taskDescription = (EditText)findViewById(R.id.taskDescriptionEditText);
        completionDate = (DatePicker)findViewById(R.id.completionDatePicker);
        completionTime = (TimePicker)findViewById(R.id.completionTimePicker);
        isCompleted = (Switch) findViewById(R.id.isCompletedSwitch);
        updateItemButton = (ImageButton)findViewById(R.id.addTodoItemButton);
        deleteItemButton = (ImageButton)findViewById(R.id.deleteTodoItemButton);

        // Get the Bundled data of the Item selected if selected
        TodoItem item = (TodoItem) getIntent().getSerializableExtra("todoItemClicked");
        int position = (int) getIntent().getIntExtra("ListIndex", 0);
        //mAdapter = (TodoItemAdapter) getIntent().getBundleExtra("todoItemAdapter");

        // If item is not null then set data to the UI/widgets, else leave empty and set the complete task switch to be invisible
        if (item != null) {
            taskTitle.setText(item.getTaskTitle());
            taskDescription.setText(item.getTaskDescription());
            isCompleted.setChecked(item.getIsCompleted() == 1 ? true : false);
            // Calculate the datepicker from the completion date int
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((long) item.getCompletionDate()) * 1000L);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(calendar.DAY_OF_MONTH);
            int hour = calendar.get(calendar.HOUR_OF_DAY);
            int minute = calendar.get(calendar.MINUTE);
            int seconds = calendar.get(calendar.SECOND);
            Log.d(TAG, "Date in UNIX: " + item.getCompletionDate());
            completionDate.init(year, month, day, null);
            completionTime.setHour(hour);
            completionTime.setMinute(minute);
        } else {
            isCompleted.setVisibility(View.GONE);
            deleteItemButton.setVisibility(View.GONE);
        }
        completionTime.setIs24HourView(true);

        // Find the bottom navigation view, assign it and set the menu item to be 1 for Details Activity
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        // Set on Click Listener for the bottom nvaigation
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_todo_list:
                        // Load Intent for Main Activity and finish the Details Activity
                        Intent intentTodoList = new Intent(DetailsActivity.this, MainActivity.class);
                        //startActivity(intentTodoList);
                        finish();
                        break;

                    case R.id.navigation_todo_details:
                        // Refresh Details Activity
                        finish();
                        startActivity(getIntent());
                        break;
                }
                return false;
            }
        });

        // On Click Listener for the is completed switch
        isCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new checked input from the switch
                int itemIsCompleted = isCompleted.isChecked() == true ? 1 : 0;
                // Update database with the the new is checked input and test to see if update has worked or not
                Boolean hasUpdatedItem = MainActivity.getDB().onUpdateData(item.getTaskID(), item.getTaskTitle(), item.getTaskDescription(), item.getCompletionDate(), itemIsCompleted);
                if(hasUpdatedItem) {
                    Toast.makeText(DetailsActivity.this, "Entry " + item.getTaskID() + " " + item.getTaskTitle() + " updated.", Toast.LENGTH_SHORT).show();
                    // Update array/array list
                    MainActivity.getDatabaseList().set(position, new TodoItem(item.getTaskID(),
                            item.getTaskTitle(),
                            item.getTaskDescription(),
                            item.getCompletionDate(),
                            itemIsCompleted));
                    /*MainActivity.getDatabaseList().set(position, new TodoItem(MainActivity.getDatabaseList().get(position).getTaskID(),
                            MainActivity.getDatabaseList().get(position).getTaskTitle(),
                            MainActivity.getDatabaseList().get(position).getTaskDescription(),
                            MainActivity.getDatabaseList().get(position).getCompletionDate(),
                            MainActivity.getDatabaseList().get(position).getIsCompleted()));*/
                } else {
                    Toast.makeText(DetailsActivity.this, "Entry " + item.getTaskID() + " " + item.getTaskTitle() + " has not updated.", Toast.LENGTH_SHORT).show();
                }
                //mAdapter.notifyDataSetChanged();
                MainActivity.getListViewAdapter().notifyDataSetChanged();
                finish();
            }
        });

        // On Click Listener for the insert/update button
        updateItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get all the data from the UI/widgets
                String itemTaskTitle = taskTitle.getText().toString();
                String itemTaskDescription = taskDescription.getText().toString();
                int itemIsCompleted = isCompleted.isChecked() == true ? 1 : 0;
                int itemCompletionDate = datePickerTimeToTimeStamp(completionDate.getYear(), completionDate.getMonth(), completionDate.getDayOfMonth(), completionTime.getHour(), completionTime.getMinute(), 0);
                // Check if item is not null if so then update data to database and check if update has worked or not
                if(item != null) {
                    Boolean hasUpdatedItem = MainActivity.getDB().onUpdateData(item.getTaskID(), itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted);
                    if(hasUpdatedItem) {
                        Toast.makeText(DetailsActivity.this, "Entry " + item.getTaskID() + " " + itemTaskTitle + " updated.", Toast.LENGTH_SHORT).show();
                        MainActivity.getDatabaseList().set(position, new TodoItem(item.getTaskID(), itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted));
                    } else {
                        Toast.makeText(DetailsActivity.this, "Entry " + item.getTaskID() + " " + itemTaskTitle + " has not updated.", Toast.LENGTH_SHORT).show();
                    }
                } else { // Else must be insert data, insert data from the UI/widgets, check if insert has worked or not
                    Boolean hasInsertedItem = MainActivity.getDB().onInsertData(itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted);
                    if(hasInsertedItem) {
                        Toast.makeText(DetailsActivity.this, "Entry " + MainActivity.getDB().onGetData().getCount() + 1 + " " + itemTaskTitle + " inserted.", Toast.LENGTH_SHORT).show();
                        // Insert into array/array list
                        MainActivity.getDatabaseList().add(new TodoItem(MainActivity.getResultData().getCount() + 1, itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted));
                    } else {
                        Toast.makeText(DetailsActivity.this, "Entry " + item.getTaskID() + " " + itemTaskTitle + " has not been inserted.", Toast.LENGTH_SHORT).show();
                    }
                }
                //mAdapter.notifyDataSetChanged();
                MainActivity.getListViewAdapter().notifyDataSetChanged();
                finish();
            }
        });

        // On Click Listener for the delete button
        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete data and check if it has worked or not
                Boolean hasDeletedItem = MainActivity.getDB().onDeleteData(item.getTaskID());
                if(hasDeletedItem) {
                    Toast.makeText(DetailsActivity.this, "Entry " + item.getTaskID() + " " + item.getTaskTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                    // Remove data from array/array list
                    MainActivity.getDatabaseList().remove(position);
                } else {
                    Toast.makeText(DetailsActivity.this, "Entry " + item.getTaskID() + " " + item.getTaskTitle() + " has not deleted.", Toast.LENGTH_SHORT).show();
                }
                //mAdapter.notifyDataSetChanged();
                MainActivity.getListViewAdapter().notifyDataSetChanged();
                finish();
            }
        });
    }
}

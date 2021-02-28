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
        //Bundle bundle = getIntent().getBundleExtra("itemClicked");
        //TodoItem item = new TodoItem(bundle.getString("taskTitle"), bundle.getString("taskDescription"), bundle.getInt("completionDate"), bundle.getInt("isCompleted"));

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
                        //Intent intentTodoDetails = new Intent(DetailsActivity.this, DetailsActivity.class);
                        //startActivity(intentTodoDetails);
                        break;
                }
                return false;
            }
        });

        isCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCompleted.isChecked()) {
                    String itemTaskTitle = taskTitle.getText().toString();
                    String itemTaskDescription = taskDescription.getText().toString();
                    int itemCompletionDate = completionDate.getYear() * 10000 + completionDate.getMonth() * 100 + completionDate.getYear() % 100;
                    Log.d(TAG, "Date in UNIX: " + itemCompletionDate);
                    int itemIsCompleted = isCompleted.isChecked() == true ? 1 : 0;
                    Boolean hasUpdatedItem = MainActivity.getDB().onUpdateData(itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted);
                    if(hasUpdatedItem) {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " updated.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailsActivity.this, "Entry " + itemTaskTitle + " has not updated.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }
        });

        updateItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {

                } catch (Exception e) {

                }*/
                Calendar calendar = Calendar.getInstance();
                String itemTaskTitle = taskTitle.getText().toString();
                String itemTaskDescription = taskDescription.getText().toString();
                int itemCompletionDate = completionDate.getYear() * 10000 + completionDate.getMonth() * 100 + completionDate.getYear() % 100;
                int itemIsCompleted = isCompleted.isChecked() == true ? 1 : 0;
                if(MainActivity.getDB().onHasDataInTable(itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted)) {
                    Boolean hasUpdatedItem = MainActivity.getDB().onUpdateData(itemTaskTitle, itemTaskDescription, itemCompletionDate, itemIsCompleted);
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
                finish();
            }
        });

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemToDelete = taskTitle.getText().toString();
                Boolean hasDeletedItem = MainActivity.getDB().onDeleteData(itemToDelete);
                if(hasDeletedItem) {
                    Toast.makeText(DetailsActivity.this, "Entry " + itemToDelete + " deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, "Entry " + itemToDelete + " has not deleted.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }
}

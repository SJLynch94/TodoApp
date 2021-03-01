package com.slynch.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    // Member variables to be used within the activity for finding the UI/widgets
    private TextView mAppTitle;
    private ImageButton mAddTodoItem;
    private ListView mListView;

    // Member variables to hold the array of data from the database, adapter for the list view
    private TodoItem[] mDatabaseItems;
    private int mLastItemUpdate = -1;
    private TodoItemAdapter mListViewAdapter;

    private final int mRefreshTime = 1000;

    // Static Database
    private static DBHelper mTodoItemsDatabase;
    public static DBHelper getDB() { return mTodoItemsDatabase; }

    // Merge sort class to sort the data by completion date
    private static MergeSort mergeSort;
    public static MergeSort getMergeSort() { return mergeSort; }

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");
        // Find List View and Add Button
        mListView = (ListView) findViewById(R.id.todoList);
        mAddTodoItem = (ImageButton) findViewById(R.id.addTodoItemButton);
        // Initialize DB and Merge Sort
        mTodoItemsDatabase = new DBHelper(this);
        mergeSort = new MergeSort();

        // Find the bottom navigation view, assign it and set the menu item to be default 0
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        // Set on Click Listener for the bottom nvaigation
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    // Refresh Main Activity if selected
                    case R.id.navigation_todo_list:
                        finish();
                        startActivity(getIntent());
                        break;
                        // Load Details Activity if selected
                    case R.id.navigation_todo_details:
                        Intent intentTodoDetails = new Intent(MainActivity.this, DetailsActivity.class);
                        startActivity(intentTodoDetails);
                        break;
                }
                return false;
            }
        });

        try {
            // Get data from database
            Cursor resultData = mTodoItemsDatabase.onGetData();
            if(resultData.getCount() != 0) {
                mDatabaseItems = new TodoItem[resultData.getCount()];
                int counter = 0;
                // Loop over database cursor data
                while(resultData.moveToNext())
                {
                    Log.d(TAG, "Items: " + resultData.getInt(0) + ", " +resultData.getString(1) + ", " + resultData.getString(2) + ", " + resultData.getInt(3) + ", " + resultData.getInt(4));
                    TodoItem item = new TodoItem(resultData.getInt(0), resultData.getString(1), resultData.getString(2), resultData.getInt(3), resultData.getInt(4));
                    mDatabaseItems[counter] = item;
                    ++counter;
                }
            }

            // Only sort if data array is not null
            if(mDatabaseItems != null) {
                mergeSort.sort(mDatabaseItems, 0, mDatabaseItems.length - 1);
            }

            // Initialise adapter and set adapter to the list view
            mListViewAdapter = new TodoItemAdapter(this, R.layout.row, mDatabaseItems);
            mListView.setAdapter(mListViewAdapter);
            // Set on Click Listener for the item to be selected
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mLastItemUpdate = position;
                    Toast.makeText(MainActivity.this, "Clicked Item: " + mDatabaseItems[position].getTaskTitle(), Toast.LENGTH_SHORT).show();
                    // Create Intent to load Details Activity, Create Bundle to pass the selected data over, put serialized data into Bundle and the Bundle into the Intent
                    Intent intentTodoDetails = new Intent(MainActivity.this, DetailsActivity.class);
                    Bundle itemBundle = new Bundle();
                    TodoItem item = new TodoItem(mDatabaseItems[position].getTaskID(), mDatabaseItems[position].getTaskTitle(), mDatabaseItems[position].getTaskDescription(), mDatabaseItems[position].getCompletionDate(), mDatabaseItems[position].getIsCompleted());
                    itemBundle.putSerializable("todoItemClicked", item);
                    intentTodoDetails.putExtras(itemBundle);
                    startActivity(intentTodoDetails);
                }
            });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "No Todo Item Entries Exist. Please create a Todo Item.", Toast.LENGTH_SHORT).show();
        }

        // On Click Listener for new TodoItem to be added
        mAddTodoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to load Details Activity
                Intent intentTodoDetails = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intentTodoDetails);
            }
        });

    }
}
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

    // Member variables
    private TextView mAppTitle;
    private ImageButton mAddTodoItem;
    private ListView mListView;
    private TodoItem[] mDatabaseItems;
    private int mLastItemUpdate = -1;
    private TodoItemAdapter mListViewAdapter;

    // Static Database
    private static DBHelper mTodoItemsDatabase;
    public static DBHelper getDB() { return mTodoItemsDatabase; }

    private static MergeSort mergeSort;
    public static MergeSort getMergeSort() { return mergeSort; }

    private static final String TAG = "MainActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_todo_list:
                    mAppTitle.setText(R.string.navigation_todo_list);
                    return true;
                case R.id.navigation_todo_details:
                    mAppTitle.setText(R.string.navigation_todo_details);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");
        mListView = (ListView) findViewById(R.id.todoList);
        //mRecyclerListView = (RecyclerView.Recycler) findViewById(R.id.todoRecyclerList);
        mAddTodoItem = (ImageButton) findViewById(R.id.addTodoItemButton);
        mTodoItemsDatabase = new DBHelper(this);
        mergeSort = new MergeSort();

        /*File data = Environment.getDataDirectory();
        String currentDBPath = "/data/com.slynch.todoapp/databases/" + "TodoItemDatabase.db";
        File currentDB = new File(data, currentDBPath);
        boolean deleted = SQLiteDatabase.deleteDatabase(currentDB);*/


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_todo_list:
                        finish();
                        startActivity(getIntent());
                        break;

                    case R.id.navigation_todo_details:
                        Intent intentTodoDetails = new Intent(MainActivity.this, DetailsActivity.class);
                        startActivity(intentTodoDetails);
                        break;
                }
                return false;
            }
        });

        try {
            Cursor resultData = mTodoItemsDatabase.onGetData();
            if(resultData.getCount() != 0) {
                mDatabaseItems = new TodoItem[resultData.getCount()];
                int counter = 0;
                while(resultData.moveToNext())
                {
                    Log.d(TAG, "Items: " + resultData.getString(0) + " " + resultData.getString(1) + " " + resultData.getInt(2) + " " + resultData.getInt(3));
                    TodoItem item = new TodoItem(resultData.getInt(0), resultData.getString(1), resultData.getString(2), resultData.getInt(3), resultData.getInt(4));
                    mDatabaseItems[counter] = item;
                    ++counter;
                }
            }

            mergeSort.sort(mDatabaseItems, 0, mDatabaseItems.length - 1);

            mListViewAdapter = new TodoItemAdapter(this, R.layout.row, mDatabaseItems);
            mListView.setAdapter(mListViewAdapter);//(new TodoItemAdapter(this, R.layout.row, mDatabaseItems));
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mLastItemUpdate = position;
                    Toast.makeText(MainActivity.this, "Clicked Item: " + mDatabaseItems[position].toString(), Toast.LENGTH_SHORT).show();
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

        // On Click Listener for new TodoItem to be
        mAddTodoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTodoDetails = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intentTodoDetails);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLastItemUpdate != -1) {
            mListViewAdapter.updateItem(mLastItemUpdate, mDatabaseItems[mLastItemUpdate]);
        }
    }
}
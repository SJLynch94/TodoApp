package com.slynch.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private TextView mAppTitle;
    private ImageButton mAddTodoItem;
    private ListView mListView;
    private TodoItem[] mDatabaseItems;
    private ArrayList<TodoItem> mDatabaseList;
    //private RecyclerView.Recycler mRecyclerListView;

    //private static BottomNavigationView navigation;
    //public static BottomNavigationView getNavigation() {return navigation;}

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
                        //Intent intentTodoList = new Intent(MainActivity.this, MainActivity.class);
                        //startActivity(intentTodoList);
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
            Cursor result = mTodoItemsDatabase.onGetData();
            try {

            } catch (Exception e) {

            }
        } catch (Exception e) {

        }
        Cursor resultData = mTodoItemsDatabase.onGetData();
        if(resultData.getCount() == 0) {
            Toast.makeText(MainActivity.this, "No Todo Item Entries Exist.", Toast.LENGTH_SHORT).show();
            getDB().onInsertData("Todo Task Title", "Todo Task Description", 0, 0);
        } else {
            //mDatabaseList = new ArrayList<>();
            //mDatabaseItems = new TodoItem[10];
            /*for (int i = 0; i < 10; ++i)
            {
                int date = 1614440170 + 1000000 - (i * 10000);
                TodoItem item = new TodoItem("Fix Database", "Fix the storing of todo items into the database.", date, 0);
                Log.d(TAG, "Date in UNIX: " + date);
                mDatabaseItems[i] = item;
                //mDatabaseList.add(item);
            }*/
            mDatabaseItems = new TodoItem[resultData.getCount()];
            int counter = 0;
            while(resultData.moveToNext())
            {
                Log.d(TAG, "Size of Database into Array: " + (counter + 1));
                TodoItem item = new TodoItem(resultData.getString(0), resultData.getString(1), resultData.getInt(2), resultData.getInt(3));
                mDatabaseItems[counter] = item;
                ++counter;
            }
        }

        mergeSort.sort(mDatabaseItems, 0, mDatabaseItems.length - 1);

        mListView.setAdapter(new TodoItemAdapter(this, R.layout.row, mDatabaseItems));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Clicked Item: " + mDatabaseItems[position].toString(), Toast.LENGTH_SHORT).show();
                Intent intentTodoDetails = new Intent(MainActivity.this, DetailsActivity.class);
                Bundle itemBundle = new Bundle();
                TodoItem item = new TodoItem(mDatabaseItems[position].getTaskTitle(), mDatabaseItems[position].getTaskDescription(), mDatabaseItems[position].getCompletionDate(), mDatabaseItems[position].getIsCompleted());
                itemBundle.putSerializable("todoItemClicked", item);
                intentTodoDetails.putExtras(itemBundle);
                startActivity(intentTodoDetails);
            }
        });


        mAddTodoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTodoDetails = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intentTodoDetails);
            }
        });
    }
}
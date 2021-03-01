package com.slynch.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Constants for easier usage of the database and queries
    public static final String TODO_ITEM_TABLE = "TodoItemDetails";
    public static final String TODO_TASK_ID = "taskID";
    public static final String TODO_TASK_TITLE = "taskTitle";
    public static final String TODO_TASK_DESCRIPTION = "taskDescription";
    public static final String TODO_TASK_COMPLETION_DATE = "completionDate";
    public static final String TODO_TASK_IS_COMPLETED = "isCompleted";

    public DBHelper(Context context) {
        super(context, "TodoItemDatabase.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +  TODO_ITEM_TABLE + "(" + TODO_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TODO_TASK_TITLE + " TEXT, " + TODO_TASK_DESCRIPTION + " TEXT, " + TODO_TASK_COMPLETION_DATE + " INTEGER, " + TODO_TASK_IS_COMPLETED + " INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_ITEM_TABLE);
        onCreate(db);
    }

    public void onDelete() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TODO_ITEM_TABLE);
    }

    // Insert function of the data into the database table
    public Boolean onInsertData(String taskTitle, String taskDescription, int completionDate, int isCompleted)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODO_TASK_TITLE, taskTitle);
        contentValues.put(TODO_TASK_DESCRIPTION, taskDescription);
        contentValues.put(TODO_TASK_COMPLETION_DATE, completionDate);
        contentValues.put(TODO_TASK_IS_COMPLETED, isCompleted);
        return db.insert(TODO_ITEM_TABLE, null, contentValues) != -1;
    }

    // Update function to update pre-existing data into the database table
    public Boolean onUpdateData(int taskID, String taskTitle, String taskDescription, int completionDate, int isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODO_TASK_TITLE, taskTitle);
        contentValues.put(TODO_TASK_DESCRIPTION, taskDescription);
        contentValues.put(TODO_TASK_COMPLETION_DATE, completionDate);
        contentValues.put(TODO_TASK_IS_COMPLETED, isCompleted);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TODO_ITEM_TABLE + " WHERE " + TODO_TASK_ID + "=?", new String[]{Integer.toString(taskID)});
        if(cursor.getCount() <= 0) {
            return false;
        }
        return db.update(TODO_ITEM_TABLE, contentValues,  TODO_TASK_ID + "=?", new String[]{Integer.toString(taskID)}) != -1;
    }

    // Delete the task from the table
    public Boolean onDeleteData(int taskID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TODO_ITEM_TABLE + " WHERE " + TODO_TASK_ID + "=?", new String[]{Integer.toString(taskID)});
        if(cursor.getCount() <= 0) {
            return false;
        }
        return db.delete(TODO_ITEM_TABLE, TODO_TASK_ID + "=?", new String[]{Integer.toString(taskID)}) != -1;
    }

    // Get data from the table within the database
    public Cursor onGetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TODO_TASK_ID + ", " + TODO_TASK_TITLE + ", " + TODO_TASK_DESCRIPTION + ", " + TODO_TASK_COMPLETION_DATE + ", " + TODO_TASK_IS_COMPLETED + " FROM " + TODO_ITEM_TABLE, null);
        return cursor;
    }
}

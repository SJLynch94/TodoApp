package com.slynch.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TODO_ITEM_TABLE = "TodoItemDetails";
    public static final String TODO_TASK_ID = "taskID";
    public static final String TODO_TASK_TITLE = "taskTitle";
    public static final String TODO_TASK_DESCRIPTION = "taskDescription";
    public static final String TODO_TASK_COMPLETION_DATE = "completionDate";
    public static final String TODO_TASK_IS_COMPLETED = "isCompleted";

    public DBHelper(Context context) {
        super(context, "TodoItemDatabase.db",null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +  TODO_ITEM_TABLE + "(" + TODO_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TODO_TASK_TITLE + " TEXT, " + TODO_TASK_DESCRIPTION + " TEXT, " + TODO_TASK_COMPLETION_DATE + " INTEGER, " + TODO_TASK_IS_COMPLETED + " INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_ITEM_TABLE);
    }

    public void onDelete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_ITEM_TABLE, null, null);
    }

    /*@Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table TodoItemDetails(name TEXT primary key, contact TEXT, dob TEXT)");
    }*/

    /*@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists TodoItemDetails");
    }*/

    public Boolean onHasDataInTable(String taskTitle, String taskDescription, int completionDate, int isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TODO_ITEM_TABLE + " where name = ?", new String[]{taskTitle});
        return cursor.getCount() > 0;
    }

    public Boolean onHasDataInTable(int taskID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TODO_ITEM_TABLE + " where id = ?", new String[]{String.valueOf(taskID)});
        return cursor.getCount() > 0;
    }

    public Boolean onInsertData(String taskTitle, String taskDescription, int completionDate, int isCompleted)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("taskTitle", taskTitle);
        contentValues.put("taskDescription", taskDescription);
        contentValues.put("completionDate", completionDate);
        contentValues.put("isCompleted", isCompleted);
        //long result =
        return db.insert(TODO_ITEM_TABLE, null, contentValues) != -1;

        /*if (result == -1) {
            return false;
        } else {
            return true;
        }*/
    }


    public Boolean onUpdateData(String taskTitle, String taskDescription, int completionDate, int isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("taskTitle", taskTitle);
        contentValues.put("taskDescription", taskDescription);
        contentValues.put("completionDate", completionDate);
        contentValues.put("isCompleted", isCompleted /*== true ? 1 : 0*/);
        Cursor cursor = db.rawQuery("Select * from " + TODO_ITEM_TABLE + " where name = ?", new String[]{taskTitle});
        if(cursor.getCount() <= 0) {
            return false;
        }
        return db.update(TODO_ITEM_TABLE, contentValues, "taskTitle=?", new String[]{taskTitle}) != -1;

        /*if (cursor.getCount() > 0) {
            //long result =
            db.update("TodoItemDetails", contentValues, "taskTitle=?", new String[]{taskTitle}) != -1;
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }*/
    }


    public Boolean onDeleteData(String taskTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TODO_ITEM_TABLE + " where name = ?", new String[]{taskTitle});
        if(cursor.getCount() <= 0) {
            return false;
        }
        return db.delete(TODO_ITEM_TABLE, "taskTitle=?", new String[]{taskTitle}) != -1;

        /*if (cursor.getCount() > 0) {
            long result = db.delete("TodoItemDetails", "taskTitle=?", new String[]{taskTitle});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }*/
    }

    public Cursor onGetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TODO_ITEM_TABLE, null);
        return cursor;
    }
}

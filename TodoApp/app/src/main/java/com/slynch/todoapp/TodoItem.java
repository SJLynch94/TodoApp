package com.slynch.todoapp;

//import android.os.Build;

import java.io.Serializable;
//import java.util.Comparator;

//import androidx.annotation.RequiresApi;

//@RequiresApi(api = Build.VERSION_CODES.N)
public class TodoItem implements Serializable/*, Comparable<TodoItem>*/ {
    // Member Variables for the TodoItem
    private int mTaskID;
    private String mTaskTitle;
    private String mTaskDescription;
    private int mCompletionDate;
    private int mIsCompleted;

    // Constructor
    public TodoItem(int taskID, String taskTitle, String taskDescription, int date, int isCompleted) {
        this.mTaskID = taskID;
        this.mTaskTitle = taskTitle;
        this.mTaskDescription = taskDescription;
        this.mCompletionDate = date;
        this.mIsCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "mTaskID='" + mTaskID + '\'' +
                "mTaskTitle='" + mTaskTitle + '\'' +
                ", mTaskDescription='" + mTaskDescription + '\'' +
                ", mCompletionDate=" + mCompletionDate +
                ", mIsCompleted=" + mIsCompleted +
                '}';
    }

    /*public static final Comparator<TodoItem> COMPLETE_DATE_COMPARATOR = Comparator
            .comparing(TodoItem::getCompletionDate);*/

    // Getters and Setters
    public int getTaskID() {return mTaskID;}
    public void setTaskID(int taskID) {this.mTaskID = taskID;}

    public String getTaskTitle() {return mTaskTitle;}
    public void setTaskTitle(String taskTitle) {this.mTaskTitle = taskTitle;}

    public String getTaskDescription() {return mTaskDescription;}
    public void setTaskDescription(String taskDescription) {this.mTaskDescription = taskDescription;}

    public int getCompletionDate() {return mCompletionDate;}
    public void setCompletionDate(int date) {this.mCompletionDate = date;}

    public int getIsCompleted() {return this.mIsCompleted;}
    public void setIsCompleted(int isCompleted) {this.mIsCompleted = isCompleted;}

    /*@Override
    public int compareTo(TodoItem o) {
        return this.getCompletionDate() < o.getCompletionDate() ? -1 : (this.getCompletionDate() == o.getCompletionDate() ? 0 : 1);
    }*/
}

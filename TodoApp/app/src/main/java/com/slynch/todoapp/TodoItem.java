package com.slynch.todoapp;

import java.io.Serializable;

public class TodoItem implements Serializable {
    private int mTaskID;
    private String mTaskTitle;
    private String mTaskDescription;
    private int mCompletionDate;
    private int mIsCompleted;

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
}

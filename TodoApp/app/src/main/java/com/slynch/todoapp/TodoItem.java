package com.slynch.todoapp;

import java.io.Serializable;

public class TodoItem implements Serializable {
    private String mTaskTitle;
    private String mTaskDescription;
    private int mCompletionDate;
    private int mIsCompleted;

    public TodoItem(String taskTitle, String taskDescription, int date, int isCompleted) {
        this.mTaskTitle = taskTitle;
        this.mTaskDescription = taskDescription;
        this.mCompletionDate = date;
        this.mIsCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "mTaskTitle='" + mTaskTitle + '\'' +
                ", mTaskDescription='" + mTaskDescription + '\'' +
                ", mCompletionDate=" + mCompletionDate +
                ", mIsCompleted=" + mIsCompleted +
                '}';
    }

    public String getTaskTitle() {return mTaskTitle;}
    public void setTaskTitle(String taskTitle) {this.mTaskTitle = taskTitle;}

    public String getTaskDescription() {return mTaskDescription;}
    public void setTaskDescription(String taskDescription) {this.mTaskDescription = taskDescription;}

    public int getCompletionDate() {return mCompletionDate;}
    public void setCompletionDate(int date) {this.mCompletionDate = date;}

    public int getIsCompleted() {return this.mIsCompleted;}
    public void setIsCompleted(int isCompleted) {this.mIsCompleted = isCompleted;}

    /*public int getCompletionDateValue() {
        int year = mCompletionDate.getYear();
        int month = mCompletionDate.getMonth();
        int day = mCompletionDate.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return (int)calendar.getTimeInMillis();
    }*/
    //public void setCompletionDate(int date) {/*this.mCompletionDate = */calculateDate(date);}


    //public boolean getIsCompleted() {return this.mIsCompleted;}
    //public int getIsCompletedValue() {return this.mIsCompleted == true ? 1: 0;}
    //public void setIsCompleted(int isCompleted) {this.mIsCompleted = isCompleted == 1 ? true : false;}



    /*private void calculateDate(int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((long) date) * 1000L);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        Log.d("Date", "Date is: " + year + "/" + month + "/" + day);
        this.mCompletionDate.init(year, month, day, null);
    }*/
}

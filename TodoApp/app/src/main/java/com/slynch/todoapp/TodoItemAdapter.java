package com.slynch.todoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {

    private static final String TAG = "TodoItemAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private ViewHolder viewHolder;

    static class ViewHolder {
        TextView title;
        TextView description;
        Switch isCompleted;
        TextView completionDate;
        ImageButton deleteTodoItemButton;
    }

    public TodoItemAdapter(Context context, int resource, TodoItem[] todoItems) {
        super(context, resource, todoItems);
        mContext = context;
        mResource = resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int taskID = getItem(position).getTaskID();
        String title = getItem(position).getTaskTitle();
        String description = getItem(position).getTaskDescription();
        int completionDate = getItem(position).getCompletionDate();
        int isCompleted = getItem(position).getIsCompleted();

        View view = convertView;

        if(view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.taskTitleEditText);
            viewHolder.description = (TextView) view.findViewById(R.id.taskDescriptionEditText);
            viewHolder.isCompleted = (Switch) view.findViewById(R.id.isCompletedSwitch);
            viewHolder.completionDate = (TextView) view.findViewById(R.id.completionDateText);
            viewHolder.deleteTodoItemButton = (ImageButton) view.findViewById(R.id.deleteTodoItemButton);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, position > lastPosition ? R.anim.load_down_anim : R.anim.load_up_anim);
        view.startAnimation(animation);
        lastPosition = position;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((long) completionDate) * 1000L);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        Date date = new Date();
        date.setTime(((long) completionDate) * 1000L);
        Log.d(TAG, "Date is: " + year + "/" + month + "/" + day);
        Log.d(TAG, "Moved item: " + getItem(position).getTaskTitle());

        viewHolder.title.setText(title);
        viewHolder.description.setText(description);
        viewHolder.isCompleted.setChecked(isCompleted == 1 ? true : false);
        viewHolder.completionDate.setText(date.toString());

        viewHolder.isCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.getDB().onHasDataInTable(taskID, title, description, (int)date.getTime(), isCompleted)) {
                    Boolean hasUpdatedItem = MainActivity.getDB().onUpdateData(taskID, title, description, (int)date.getTime(), viewHolder.isCompleted.isChecked() == true ? 1 : 0);
                    if(hasUpdatedItem) {
                        Toast.makeText(v.getContext(), "Entry " + title + " updated.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "Entry " + title + " has not updated.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        viewHolder.deleteTodoItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean hasDeletedItem = MainActivity.getDB().onDeleteData(taskID);
                if(hasDeletedItem) {
                    Toast.makeText(v.getContext(), "Entry " + title + " deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Entry " + title + " has not deleted.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void updateItem(int position, TodoItem item) {

    }
}

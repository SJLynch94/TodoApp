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
import android.widget.Switch;
import android.widget.TextView;
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

    static class ViewHolder {
        TextView title;
        TextView description;
        Switch isCompleted;
        TextView completionDate;
    }

    public TodoItemAdapter(Context context, int resource, TodoItem[] todoItems) {
        super(context, resource, todoItems);
        mContext = context;
        mResource = resource;
    }

    public TodoItemAdapter(Context context, int resource, ArrayList<TodoItem> todoItems) {
        super(context, resource, todoItems);
        mContext = context;
        mResource = resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTaskTitle();
        String description = getItem(position).getTaskDescription();
        int completionDate = getItem(position).getCompletionDate();
        int isCompleted = getItem(position).getIsCompleted();

        View view = convertView;
        ViewHolder viewHolder;

        if(view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.taskTitleEditText);
            viewHolder.description = (TextView) view.findViewById(R.id.taskDescriptionEditText);
            viewHolder.isCompleted = (Switch) view.findViewById(R.id.isCompletedSwitch);
            viewHolder.completionDate = (TextView) view.findViewById(R.id.completionDateText);
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
        //viewHolder.completionDate.init(year, month, day, null);


        return view;
    }
}
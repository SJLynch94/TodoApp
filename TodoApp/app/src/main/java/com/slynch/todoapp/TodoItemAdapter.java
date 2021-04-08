package com.slynch.todoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {

    private static final String TAG = "TodoItemAdapter";
    // Member variables to be used within the adapter
    private Context mContext;
    private int mResource;
    private TodoItemAdapter mAdapter;
    private TodoItem[] mDataArray;
    private List<TodoItem> mDataList;
    private ArrayList<TodoItem> mDataArrayList;
    private int mLastPosition = -1;
    private ViewHolder mViewHolder;

    // Class to hold the UI/Widgets for smoother transitions in the list view
    static class ViewHolder {
        TextView title;
        TextView description;
        Switch isCompleted;
        TextView completionDate;
        ImageButton deleteTodoItemButton;
    }

    // Constructor with the context, resource/layout, items to be loaded in the list view
    public TodoItemAdapter(Context context, int resource, TodoItem[] todoItems) {
        super(context, resource, todoItems);
        mContext = context;
        mResource = resource;
        mDataArray = todoItems;
        mAdapter = this;
    }

    public TodoItemAdapter(Context context, int resource, List<TodoItem> todoItems) {
        super(context, resource, todoItems);
        mContext = context;
        mResource = resource;
        mDataList = todoItems;
        mAdapter = this;
    }

    public TodoItemAdapter(Context context, int resource, ArrayList<TodoItem> todoItems) {
        super(context, resource, todoItems);
        mContext = context;
        mResource = resource;
        mDataArrayList = todoItems;
        mAdapter = this;
    }

    // View function for the list view to load each items data
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data from the current position
        int taskID = getItem(position).getTaskID();
        String title = getItem(position).getTaskTitle();
        String description = getItem(position).getTaskDescription();
        int completionDate = getItem(position).getCompletionDate();
        int isCompleted = getItem(position).getIsCompleted();

        // Assign convertView to new view to be returned
        View view = convertView;

        // Check if view is null then create the layout
        if(view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(mResource, null);
            // Create new View Holder, assign each UI/widget by finding the objects
            mViewHolder = new ViewHolder();
            mViewHolder.title = (TextView) view.findViewById(R.id.taskTitleEditText);
            mViewHolder.description = (TextView) view.findViewById(R.id.taskDescriptionEditText);
            mViewHolder.isCompleted = (Switch) view.findViewById(R.id.isCompletedSwitch);
            mViewHolder.completionDate = (TextView) view.findViewById(R.id.completionDateText);
            mViewHolder.deleteTodoItemButton = (ImageButton) view.findViewById(R.id.deleteTodoItemButton);
            view.setTag(mViewHolder);
        }
        else { // Otherwise assign the viewholder as it is already available
            mViewHolder = (ViewHolder) view.getTag();
        }

        // Create an animation to run based on the position and last position, using either load up or down, start animation and assign last position to the current position
        Animation animation = AnimationUtils.loadAnimation(mContext, position > mLastPosition ? R.anim.load_down_anim : R.anim.load_up_anim);
        view.startAnimation(animation);
        mLastPosition = position;

        // Calculate date to be used to set the date picker
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((long) completionDate) * 1000L);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        Date date = new Date();
        date.setTime(((long) completionDate) * 1000L);
        Log.d(TAG, "Date is: " + year + "/" + month + "/" + day);

        // Set data to the UI/widgets
        mViewHolder.title.setText(title);
        mViewHolder.description.setText(description);
        mViewHolder.isCompleted.setChecked(isCompleted == 1 ? true : false);
        mViewHolder.completionDate.setText(date.toString());

        // On Click Listener for the switch within the list view
        mViewHolder.isCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update and check if it has worked or not
                Boolean hasUpdatedItem = MainActivity.getDB().onUpdateData(taskID, title, description, completionDate, mViewHolder.isCompleted.isChecked() == true ? 1 : 0);
                if(hasUpdatedItem) {
                    Toast.makeText(v.getContext(), "Entry " + taskID + " " + title + " updated.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Entry " + taskID + " " + title + " has not updated.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // On Click Listener for the delete button within the list view
        mViewHolder.deleteTodoItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete and check if it has worked or not
                Boolean hasDeletedItem = MainActivity.getDB().onDeleteData(taskID);
                if(hasDeletedItem) {
                    Toast.makeText(v.getContext(), "Entry " + taskID + " " +title + " deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Entry " + taskID + " " + title + " has not deleted.", Toast.LENGTH_SHORT).show();
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}

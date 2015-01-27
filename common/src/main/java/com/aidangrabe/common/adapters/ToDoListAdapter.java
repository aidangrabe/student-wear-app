package com.aidangrabe.common.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidangrabe.common.R;
import com.aidangrabe.common.model.todolist.ToDoItem;

/**
 * Created by aidan on 25/01/15.
 * A simple ArrayAdapter for displaying ToDoItems
 */
public class ToDoListAdapter extends ArrayAdapter<ToDoItem> {

    public ToDoListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_todo, parent, false);
        }

        ToDoItem item = getItem(position);

        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setTextColor(getContext().getResources().getColor(R.color.text_color));
        tv.setText(item.getTitle());

        // show the check mark if necessary
        ImageView tickImage = (ImageView) view.findViewById(R.id.img_tick);
        tickImage.setVisibility(item.isCompleted() ? View.VISIBLE : View.INVISIBLE);

        setViewComplete(tv, item.isCompleted());

        return view;

    }

    public static void setViewComplete(TextView tv, boolean completed) {
        // toggle strike through
        if (completed) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

}

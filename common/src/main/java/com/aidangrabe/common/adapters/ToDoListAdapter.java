package com.aidangrabe.common.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        View view = super.getView(position, convertView, parent);

        ToDoItem item = getItem(position);

        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setTextColor(getContext().getResources().getColor(R.color.text_color));
        tv.setText(item.getTitle());

        // toggle strike through
        if (item.isCompleted()) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return view;

    }

}

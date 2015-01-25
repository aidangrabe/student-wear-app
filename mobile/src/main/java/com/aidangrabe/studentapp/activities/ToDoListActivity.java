package com.aidangrabe.studentapp.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.aidangrabe.common.model.todolist.ToDoItem;
import com.aidangrabe.common.model.todolist.ToDoItemManager;
import com.aidangrabe.studentapp.R;
import com.aidangrabe.studentapp.fragments.ToDoListFragment;

import static android.util.Log.d;

/**
 * Created by aidan on 09/01/15.
 * Activity to show current ToDoList items
 */
public class ToDoListActivity extends ActionBarActivity {

    private static final String TAG_FRAGMENT = "ToDoListFragment";
    private ToDoListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mFragment = new ToDoListFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, mFragment, TAG_FRAGMENT).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals("Remove complete")) {
            d("D", "removing complete items");
            ToDoItemManager itemManager = new ToDoItemManager(this);
            Cursor cursor = itemManager.getComplete();
            while (cursor.moveToNext()) {
                ToDoItem toDoItem = ToDoItemManager.instanceFromCursor(cursor);
                itemManager.delete(toDoItem);
                mFragment.getToDoList();
            }
            cursor.close();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("Remove complete");
        return super.onCreateOptionsMenu(menu);
    }

}

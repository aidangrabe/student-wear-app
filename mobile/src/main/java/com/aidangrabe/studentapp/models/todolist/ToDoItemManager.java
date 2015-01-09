package com.aidangrabe.studentapp.models.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by aidan on 09/01/15.
 * Class to access SQLite Database for storing/retrieving ToDoItems
 */
public class ToDoItemManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDoList.db";
    public static final int DATABASE_VERSION = 1;

    private static ToDoItemManager mInstance;

    /**
     * Class containing the column and table names
     */
    public static abstract class ToDoEntry {
        public static final String TABLE_NAME = "todolist";
        public static final String COL_ID = "id";
        public static final String COL_TITLE = "title";
        public static final String COL_CREATE_DATE = "created";
        public static final String COL_COMPLETE_DATE = "completed";
    }

    /**
     * Class containing relevant SQL queries for the Database
     */
    public static abstract class SqlStatement {
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s)", ToDoEntry.TABLE_NAME,
                        ToDoEntry.COL_ID + " INTEGER PRIMARY KEY," +
                        ToDoEntry.COL_TITLE + " VARCHAR (255)," +
                        ToDoEntry.COL_CREATE_DATE + " INTEGER," +
                        ToDoEntry.COL_COMPLETE_DATE + " INTEGER"
        );
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + ToDoEntry.TABLE_NAME;
    }

    public ToDoItemManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get all the ToDoList items
     * @return a Cursor containing the results of all the ToDoList items
     */
    public Cursor getAll() {

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = new String[] {
                ToDoEntry.COL_ID,
                ToDoEntry.COL_TITLE,
                ToDoEntry.COL_CREATE_DATE,
                ToDoEntry.COL_COMPLETE_DATE
        };
        String orderBy = ToDoEntry.COL_CREATE_DATE + " DESC";

        return db.query(ToDoEntry.TABLE_NAME, projection, null, null, null, null, orderBy);

    }

    /**
     * Converts a ToDoItem into a ContentValues object
     * @param item the ToDoItem to convert
     * @return A ContentValues with the ToDoItem values inside
     */
    private ContentValues makeContentValues(ToDoItem item) {
        ContentValues cv = new ContentValues();
        cv.put(ToDoEntry.COL_TITLE, item.getTitle());
        cv.put(ToDoEntry.COL_CREATE_DATE, item.getCreationDate().getTime());
        cv.put(ToDoEntry.COL_COMPLETE_DATE, item.getCompletionDate().getTime());
        return cv;
    }

    public void delete(ToDoItem item) {
        if (item.getId() == -1) {
            return;
        }
        SQLiteDatabase db = getReadableDatabase();
        db.delete(ToDoEntry.TABLE_NAME, ToDoEntry.COL_ID + " = ?", new String[] {Integer.toString(item.getId())});
    }

    public void update(ToDoItem item) {
        if (item.getId() == -1) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        db.update(ToDoEntry.TABLE_NAME, makeContentValues(item), ToDoEntry.COL_ID + " = ?", new String[] {Integer.toString(item.getId())});
    }

    public void save(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(ToDoEntry.TABLE_NAME, "", makeContentValues(item));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlStatement.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqlStatement.DROP_TABLE);
        onCreate(db);
    }

    /**
     * Create a ToDoItem from a Cursor
     * @param c the Cursor object to use
     * @return a new ToDoItem filled with the contents from the cursor
     */
    public static ToDoItem instanceFromCursor(Cursor c) {

        String title = c.getString(c.getColumnIndexOrThrow(ToDoItemManager.ToDoEntry.COL_TITLE));
        int id = c.getInt(c.getColumnIndexOrThrow(ToDoItemManager.ToDoEntry.COL_ID));
        String createDateString  = c.getString(c.getColumnIndexOrThrow(ToDoEntry.COL_CREATE_DATE));
        String completeDateString = c.getString(c.getColumnIndexOrThrow(ToDoEntry.COL_COMPLETE_DATE));

        Date createDate = new Date(Long.parseLong(createDateString));
        Date completeDate = new Date(Long.parseLong(completeDateString));

        ToDoItem item = new ToDoItem(title, createDate, completeDate);
        item.setId(id);

        return item;

    }


}

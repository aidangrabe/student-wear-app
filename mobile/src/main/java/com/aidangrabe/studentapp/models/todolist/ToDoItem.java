package com.aidangrabe.studentapp.models.todolist;

import java.util.Date;

/**
 * Created by aidan on 09/01/15.
 * A class representing a single item of a ToDoList
 */
public class ToDoItem {

    private int id;
    private String title;
    private Date creationDate, completionDate;


    public ToDoItem(String title) {
        this(title, new Date());
    }

    public ToDoItem(String title, Date creationDate) {
        this(title, creationDate, null);
    }

    public ToDoItem(String title, Date creationDate, Date completionDate) {

        this.id = -1;
        this.title = title;
        this.creationDate = creationDate;
        this.completionDate = completionDate;

    }

    // getters + setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
}

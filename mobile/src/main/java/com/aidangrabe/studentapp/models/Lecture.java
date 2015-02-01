package com.aidangrabe.studentapp.models;

import android.os.Bundle;

import com.google.gson.Gson;
import com.orm.SugarRecord;

/**
 * Created by aidan on 08/01/15.
 * Should be called Class but can't do that! :P
 */
public class Lecture extends SugarRecord<Lecture> {

    private String name, location;
    private int dayOfWeek, startHour, startMinute, endHour, endMinute;

    // needed for Sugar ORM
    public Lecture() {}

    /**
     * @param name the name of the class
     * @param location the location of the class
     * @param dayOfWeek the number of the day of the week starting at 0
     * @param startHour the 24-hour start time
     * @param startMinute the start minutes
     * @param endHour the 24-hour end time
     * @param endMinute the end minutes
     */
    public Lecture(String name, String location, int dayOfWeek, int startHour, int startMinute, int endHour, int endMinute) {

        this.name = name;
        this.location = location;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;

    }

    public Bundle toBundle() {

        Bundle bundle = new Bundle();

        bundle.putString("name", name);
        bundle.putString("location", location);
        bundle.putInt("dayOfWeek", dayOfWeek);
        bundle.putInt("startHour", startHour);
        bundle.putInt("startMinute", startMinute);
        bundle.putInt("endHour", endHour);
        bundle.putInt("endMinute", endMinute);

        // Sugar ORM "Id" field
        bundle.putLong("id", getId());

        return bundle;

    }

    public static Lecture instanceFromBundle(Bundle bundle) {
        Lecture lecture = new Lecture(bundle.getString("name"),
                bundle.getString("location"),
                bundle.getInt("dayOfWeek"),
                bundle.getInt("startHour"),
                bundle.getInt("startMinute"),
                bundle.getInt("endHour"),
                bundle.getInt("endMinute")
                );

        // Sugar ORM "Id" field
        lecture.setId(bundle.getLong("id"));
        return lecture;
    }

    // getters and setters

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this, Lecture.class);
    }
}

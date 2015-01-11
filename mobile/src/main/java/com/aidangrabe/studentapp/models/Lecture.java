package com.aidangrabe.studentapp.models;

import android.content.Context;
import android.os.Bundle;

import com.aidangrabe.studentapp.util.FileUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aidan on 08/01/15.
 * Should be called Class but can't do that! :P
 */
public class Lecture {

    public static final String FILE_NAME = "class_list.json";
    public static final String KEY_CLASSES = "classes";

    private String name, location;
    private int dayOfWeek, startHour, startMinute, endHour, endMinute;

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

    /**
     * Get an ArrayList of lectures saved as JSON
     * @param context the context used for resolving the file path
     * @return a list of lectures
     * @throws IOException
     * @throws JSONException
     */
    public static ArrayList<Lecture> getSavedLectures(Context context) throws IOException, JSONException {

        String filePath = getSaveFilePath(context);
        ArrayList<Lecture> lectures = new ArrayList<>();

        // read from the class file
        String jsonContent = FileUtils.getFileContents(context.openFileInput(FILE_NAME));

        // if the file is empty, do nothing
        if (jsonContent.length() > 0) {

            // get the wrapper objects
            JSONObject json = new JSONObject(jsonContent);
            JSONArray classes = json.getJSONArray(KEY_CLASSES);

            // re-construct each lecture add each lecture to the Array
            for (int i = 0; i < classes.length(); i++) {
                Lecture lecture = new Gson().fromJson(classes.getJSONObject(i).toString(), Lecture.class);
                lectures.add(lecture);
            }
        }

        return lectures;

    }

    /**
     * Save a given list of lectures in JSON format
     * @param context the context used for resolving the file path
     * @param lectures the lecture list to save
     * @throws JSONException
     * @throws IOException
     */
    public static void saveLectures(Context context, List<Lecture> lectures) throws JSONException, IOException {

        // create the JSON data to save
        Map<String, List<Lecture>> data = new HashMap<>();
        data.put(KEY_CLASSES, lectures);
        String json = new Gson().toJson(data);

        // ensure our classes file exists
        String filePath = getSaveFilePath(context);

        // write the JSON
        // note: we can not use directories with openFileOutput, just the file name
        OutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        FileUtils.putFileContents(json, outputStream);

    }

    /**
     * Ensure that the save file exists and return the absolure path to it
     * @param context the context used for resolving the file path
     * @return the absolute file path of the save file
     * @throws IOException
     */
    public static String getSaveFilePath(Context context) throws IOException {

        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            boolean success = file.createNewFile();
            if (!success) {
                throw new IOException("Error creating file: " + FILE_NAME);
            }
        }

        return file.getAbsolutePath();
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

        return bundle;

    }

    public static Lecture instanceFromBundle(Bundle bundle) {
        return new Lecture(bundle.getString("name"),
                bundle.getString("location"),
                bundle.getInt("dayOfWeek"),
                bundle.getInt("startHour"),
                bundle.getInt("startMinute"),
                bundle.getInt("endHour"),
                bundle.getInt("endMinute")
                );
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

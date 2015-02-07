package com.aidangrabe.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by aidan on 31/01/15.
 *
 */
public class Module extends SugarRecord<Module> implements Parcelable {

    private String name;

    public Module() {}

    public Module(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get this Module's results as a List
     * @return a List of Results
     */
    public List<Result> listAllResults() {

        return Result.find(Result.class, "module = ?", Long.toString(getId()));

    }

    public float getResultSum() {
        return getResultSum(listAllResults());
    }

    public float getResultSum(List<Result> results) {

        float sum = 0;
        for (Result result : results) {
            sum += result.getGrade();
        }

        return sum;

    }

    public float getResultAverage() {

        List<Result> results = listAllResults();

        return results.size() > 0 ? getResultSum(results) / results.size() : 0;

    }


    // Parcel methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(getId());
    }

    public static final Creator<Module> CREATOR = new Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel source) {
            Module module = new Module(source.readString());
            module.setId(source.readLong());
            return module;
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[0];
        }
    };

}

package com.aidangrabe.common.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by aidan on 31/01/15.
 *
 */
public class Module extends SugarRecord<Module> {

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

}

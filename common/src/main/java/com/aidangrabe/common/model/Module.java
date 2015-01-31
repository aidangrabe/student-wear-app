package com.aidangrabe.common.model;

import com.orm.SugarRecord;

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
}

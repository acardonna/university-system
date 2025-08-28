package com.solvd.university.model;

public class Building extends Facility {

    public Building(String name) {
        super(name);
    }

    public Building() {
    }

    @Override
    public String toString() {
        return "Building{"
                + "name='" + name + '\''
                + '}';
    }
}

package com.solvd.university.model;

public enum GradeLevel {
    FRESHMAN("Freshman", 1),
    SOPHOMORE("Sophomore", 2),
    JUNIOR("Junior", 3),
    SENIOR("Senior", 4),
    GRADUATE("Graduate", 5);

    private final String displayName;
    private final int year;

    GradeLevel(String displayName, int year) {
        this.displayName = displayName;
        this.year = year;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

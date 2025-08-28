package com.solvd.university.model;

public enum CourseDifficulty {
    INTRODUCTORY("Introductory", 1),
    INTERMEDIATE("Intermediate", 2),
    ADVANCED("Advanced", 3),
    GRADUATE("Graduate", 4);

    private final String displayName;
    private final int level;

    CourseDifficulty(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

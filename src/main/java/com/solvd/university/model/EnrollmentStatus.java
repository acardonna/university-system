package com.solvd.university.model;

public enum EnrollmentStatus {
    ACTIVE("Active", "Student is currently enrolled and in good standing"),
    INACTIVE("Inactive", "Student is not currently enrolled");


    private final String displayName;
    private final String description;

    EnrollmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

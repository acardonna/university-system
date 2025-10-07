package com.solvd.university.model;

public abstract class Staff extends Person {

    protected Department<?> department;
    protected String title;

    protected Staff() {}

    protected Staff(String firstName, String lastName, String email, Department<?> department, String title) {
        super(firstName, lastName, email);
        this.department = department;
        this.title = title;
    }

    public Department<?> getDepartment() {
        return department;
    }

    public void setDepartment(Department<?> department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

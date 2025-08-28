package com.solvd.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Professor extends Staff implements Teachable {

    private final List<Course<?, ?>> assignedCourses = new ArrayList<>();

    public Professor(String firstName, String lastName, Department<?> department, String title) {
        super(firstName, lastName, firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.edu", department, title);
    }

    public Professor() {
        super();
    }

    @Override
    public void assignCourse(Course<?, ?> course) {
        if (course == null) {
            return;
        }

        if (course.getProfessor() != this) {
            course.setProfessor(this);
        }

        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }

    public List<Course<?, ?>> getAssignedCourses() {
        return assignedCourses;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Professor professor = (Professor) o;
        return Objects.equals(firstName, professor.firstName) && Objects.equals(lastName, professor.lastName) && Objects.equals(department, professor.department) && Objects.equals(title, professor.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, department, title);
    }

    @Override
    public String toString() {
        return String.format("%s %s | %s Department",
                title, getFullName(), department.getName());
    }
}

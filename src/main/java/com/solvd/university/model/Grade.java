package com.solvd.university.model;

import java.util.Objects;

public class Grade<T> {
    private String subject;
    private T value;
    private String semester;

    public Grade(String subject, T value, String semester) {
        this.subject = subject;
        this.value = value;
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public T getValue() {
        return value;
    }

    public String getSemester() {
        return semester;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade<?> grade = (Grade<?>) o;
        return Objects.equals(subject, grade.subject) &&
               Objects.equals(value, grade.value) &&
               Objects.equals(semester, grade.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, value, semester);
    }

    @Override
    public String toString() {
        return String.format("Grade{subject='%s', value=%s, semester='%s'}",
                           subject, value, semester);
    }
}
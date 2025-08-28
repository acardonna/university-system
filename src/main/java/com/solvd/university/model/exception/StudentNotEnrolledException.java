package com.solvd.university.model.exception;

/**
 * Exception thrown when a student attempts to perform actions that require enrollment but they are not enrolled.
 */
public class StudentNotEnrolledException extends Exception {

    public StudentNotEnrolledException() {
        super("Student is not enrolled in any program. Please enroll in a program first.");
    }

    public StudentNotEnrolledException(String message) {
        super(message);
    }
}

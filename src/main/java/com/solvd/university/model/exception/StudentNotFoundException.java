package com.solvd.university.model.exception;

/**
 * Exception thrown when authentication fails during student login.
 */
public class StudentNotFoundException extends Exception {

    public StudentNotFoundException() {
        super("Student not found. Please check your credentials.");
    }

    public StudentNotFoundException(String message) {
        super(message);
    }
}

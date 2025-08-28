package com.solvd.university.model.exception;

/**
 * Exception thrown when a student attempts to enroll in a program while already enrolled in another program.
 */
public class AlreadyEnrolledException extends Exception {

    public AlreadyEnrolledException() {
        super("Student is already enrolled in a program. Cannot enroll in multiple programs simultaneously.");
    }

    public AlreadyEnrolledException(String message) {
        super(message);
    }
}

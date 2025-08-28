package com.solvd.university.model.exception;

/**
 * Exception thrown when a student attempts to register with an email that is already in use.
 */
public class DuplicateRegistrationException extends Exception {

    public DuplicateRegistrationException() {
        super("Student with this email is already registered. Please log in instead.");
    }

    public DuplicateRegistrationException(String message) {
        super(message);
    }
}

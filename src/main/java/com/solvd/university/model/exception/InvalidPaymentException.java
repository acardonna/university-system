package com.solvd.university.model.exception;

/**
 * Exception thrown when a student attempts to make a payment that exceeds their outstanding balance.
 */
public class InvalidPaymentException extends Exception {

    public InvalidPaymentException() {
        super("Invalid payment amount. Please enter a valid payment.");
    }

    public InvalidPaymentException(String message) {
        super(message);
    }
}

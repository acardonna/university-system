package com.solvd.university.model;

import java.time.LocalDate;

public record Enrollment(Student student, Program program, LocalDate enrollmentDate, EnrollmentStatus status) {
    public Enrollment(Student student, Program program) {
        this(student, program, LocalDate.now(), EnrollmentStatus.ACTIVE);
    }

    public Enrollment {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (program == null) {
            throw new IllegalArgumentException("Program cannot be null");
        }
        if (enrollmentDate == null) {
            throw new IllegalArgumentException("Enrollment date cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
    }

    public Enrollment withStatus(EnrollmentStatus newStatus) {
        return new Enrollment(student, program, enrollmentDate, newStatus);
    }
}

package com.solvd.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Enrollment {

    private Student student;
    private Program program;
    private LocalDate enrollmentDate;
    private EnrollmentStatus status;

    public Enrollment(Student student, Program program) {
        this.student = student;
        this.program = program;
        this.enrollmentDate = LocalDate.now();
        this.status = EnrollmentStatus.ACTIVE;
    }

    public Enrollment() {
        this.status = EnrollmentStatus.INACTIVE;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(student, that.student) && Objects.equals(program, that.program) && Objects.equals(enrollmentDate, that.enrollmentDate) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, program, enrollmentDate, status);
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "student=" + student +
                ", program=" + program +
                ", enrollmentDate=" + enrollmentDate +
                ", status=" + status +
                '}';
    }
}

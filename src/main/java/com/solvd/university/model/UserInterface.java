package com.solvd.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.model.exception.AlreadyEnrolledException;
import com.solvd.university.model.exception.DuplicateRegistrationException;
import com.solvd.university.model.exception.InvalidPaymentException;
import com.solvd.university.model.exception.StudentNotEnrolledException;
import com.solvd.university.model.exception.StudentNotFoundException;

public class UserInterface {
    private static final Logger LOGGER = LogManager.getLogger();

    Scanner scanner;
    University university;

    public UserInterface(Scanner scanner, University university) {
        this.scanner = scanner;
        this.university = university;
    }

    public void start() {
        while (true) {
            entryMessage();

            int option = getIntInput("Select option: ");

            switch (option) {
                case 1 -> handleStudentRegistration();
                case 2 -> handleStudentLogin();
                case 3 -> showAvailableProgramsAsGuest();
                case 4 -> showProfessorsAndCourses();
                case 5 -> {
                    LOGGER.info("Thanks for visiting. Come back soon!");
                    return;
                }
                default -> LOGGER.info("Invalid option. Please try again.");
            }
        }
    }

    private void handleStudentRegistration() {
        LOGGER.info("=== Student Registration ===");
        LOGGER.info("");
        LOGGER.info("Enter your details:");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        int age = getIntInput("Age: ");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        LOGGER.info("");

        try {
            registerStudent(firstName, lastName, age, email);
        } catch (DuplicateRegistrationException e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("");
        }
    }

    private void registerStudent(String firstName, String lastName, int age, String email)
            throws DuplicateRegistrationException {
        boolean isAlreadyRegistered = university.getStudentRegistry().stream()
                .anyMatch(existingStudent -> existingStudent.getEmail().equalsIgnoreCase(email));

        if (isAlreadyRegistered) {
            throw new DuplicateRegistrationException(
                    "Student with email '" + email + "' is already registered. Please log in instead.");
        }

        Student student = new Student(firstName, lastName, age, email);
        student.setRegistered(true);
        university.registerStudent(student);

        LOGGER.info("Registration successful! Now you can enroll in a program.");
        LOGGER.info("Use the following credentials to log in: ");
        LOGGER.info("Email: " + student.getEmail());
        LOGGER.info("Student ID: " + student.getStudentId());
        LOGGER.info("");

    }

    private void handleStudentLogin() {
        LOGGER.info("=== Student Login ===");
        LOGGER.info("");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        int studentId = getIntInput("Student ID: ");

        try {
            Student student = authenticateStudent(email, studentId);
            loginDashboard(student);
        } catch (StudentNotFoundException e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("");
        }
    }

    private Student authenticateStudent(String email, int studentId) throws StudentNotFoundException {
        return university.getStudentRegistry().stream()
                .filter(student -> student.getEmail().equals(email))
                .filter(student -> student.getStudentId() == studentId)
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("No student found with email '" + email
                        + "' and Student ID '" + studentId + "'. Please check your credentials."));
    }

    private void entryMessage() {

        LOGGER.info(String.format("=== %s University Enrollment System ===", university.getName()));
        LOGGER.info("");

        LOGGER.info("1. Register as new student.");
        LOGGER.info("2. Log in as existing student.");
        LOGGER.info("3. Browse programs as guest");
        LOGGER.info("4. View popular courses, professors, and others");
        LOGGER.info("5. Exit");
    }

    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                LOGGER.info("Please enter a valid number.");
                LOGGER.info("");
            }
        }

    }

    private void loginDashboard(Student student) {
        LOGGER.info("Login successful!");
        LOGGER.info("");

        LOGGER.info(String.format("Welcome, %s!", student.getFullName()));
        LOGGER.info(String.format("Academic Level: %s (Year %d)",
                student.getGradeLevel().getDisplayName(),
                student.getGradeLevel().getYear()));
        LOGGER.info("");

        String statusMessage = student.isEnrolled()
                ? String.format("You are enrolled in %s (Status: %s)",
                        student.getEnrolledProgram().getName(),
                        student.getEnrollmentStatus().getDisplayName())
                : "No enrollment yet (Status: " + student.getEnrollmentStatus().getDisplayName() + ")";

        LOGGER.info("Current Status: " + statusMessage);

        while (true) {
            LOGGER.info("1. View my enrollment");
            LOGGER.info("2. Enroll in a program");
            LOGGER.info("3. Make a payment");
            LOGGER.info("4. View my grades");
            LOGGER.info("5. Log out");
            LOGGER.info("");

            int option = getIntInput("Select option: ");

            switch (option) {
                case 1 -> {
                    try {
                        viewEnrollmentDetails(student);
                    } catch (StudentNotEnrolledException e) {
                        LOGGER.error(e.getMessage());
                        LOGGER.info("");
                    }
                }
                case 2 -> {
                    try {
                        handleEnrollment(student);
                    } catch (AlreadyEnrolledException e) {
                        LOGGER.error(e.getMessage());
                        LOGGER.info("");
                    }
                }
                case 3 -> {
                    try {
                        handlePayment(student);
                    } catch (StudentNotEnrolledException | InvalidPaymentException e) {
                        LOGGER.error(e.getMessage());
                        LOGGER.info("");
                    }
                }
                case 4 -> {
                    viewStudentGrades(student);
                }
                case 5 -> {
                    return;
                }
                default -> LOGGER.info("Invalid option. Please try again.");
            }
        }
    }

    // TODO: implement the functionality to manually add grades
    private void viewStudentGrades(Student student) {
        LOGGER.info("=== My Academic Record ===");
        LOGGER.info("");

        List<Grade<Double>> grades = student.getGrades();

        if (grades.isEmpty()) {
            LOGGER.info("No grades recorded yet.");
            LOGGER.info("");
            return;
        }

        LOGGER.info(String.format("Overall Average: %.2f", student.calculateAverageGrade()));
        LOGGER.info(String.format("Total Courses: %d", grades.size()));
        LOGGER.info("");

        Map<String, List<Grade<Double>>> gradesBySemester = grades.stream()
                .collect(Collectors.groupingBy(Grade::getSemester));

        gradesBySemester.entrySet().forEach(entry -> {
            String semester = entry.getKey();
            List<Grade<Double>> semesterGrades = entry.getValue();

            LOGGER.info(String.format("=== %s ===", semester));
            LOGGER.info(String.format("Semester Average: %.2f", student.calculateSemesterAverage(semester)));
            LOGGER.info("");

            semesterGrades.forEach(grade -> {
                String letterGrade = convertToLetterGrade(grade.getValue());
                LOGGER.info(String.format("  %-30s | %.1f (%s)",
                        grade.getSubject(), grade.getValue(), letterGrade));
            });
            LOGGER.info("");
        });

        long aGrades = grades.stream().filter(g -> g.getValue() >= 90).count();
        long bGrades = grades.stream().filter(g -> g.getValue() >= 80 && g.getValue() < 90).count();
        long cGrades = grades.stream().filter(g -> g.getValue() >= 70 && g.getValue() < 80).count();
        long dGrades = grades.stream().filter(g -> g.getValue() >= 60 && g.getValue() < 70).count();
        long fGrades = grades.stream().filter(g -> g.getValue() < 60).count();

        LOGGER.info("=== Grade Distribution ===");
        LOGGER.info(String.format("A's: %d | B's: %d | C's: %d | D's: %d | F's: %d",
                aGrades, bGrades, cGrades, dGrades, fGrades));
        LOGGER.info("");
    }

    private String convertToLetterGrade(double numericGrade) {
        if (numericGrade >= 97)
            return "A+";
        else if (numericGrade >= 93)
            return "A";
        else if (numericGrade >= 90)
            return "A-";
        else if (numericGrade >= 87)
            return "B+";
        else if (numericGrade >= 83)
            return "B";
        else if (numericGrade >= 80)
            return "B-";
        else if (numericGrade >= 77)
            return "C+";
        else if (numericGrade >= 73)
            return "C";
        else if (numericGrade >= 70)
            return "C-";
        else if (numericGrade >= 67)
            return "D+";
        else if (numericGrade >= 63)
            return "D";
        else if (numericGrade >= 60)
            return "D-";
        else
            return "F";
    }

    private void viewEnrollmentDetails(Student student) throws StudentNotEnrolledException {
        if (!student.isEnrolled()) {
            throw new StudentNotEnrolledException(
                    "Student must be enrolled in a program to view enrollment details. Please enroll in a program first.");
        }

        Enrollment studentEnrollment = university.getEnrollments().stream()
                .filter(enrollment -> enrollment.getStudent().equals(student))
                .findFirst()
                .orElse(null);

        LOGGER.info("=== Enrollment Details ===");
        LOGGER.info("");

        LOGGER.info("Student: " + student.getFullName());
        LOGGER.info("Academic Level: " + student.getGradeLevel().getDisplayName() + " (Year "
                + student.getGradeLevel().getYear() + ")");
        LOGGER.info("Program: " + student.getEnrolledProgram().getName());
        LOGGER.info("Department: " + student.getEnrolledProgram().getDepartment().getName());
        LOGGER.info("Program Price: " + student.getEnrolledProgram().getPrice());
        LOGGER.info("Outstanding Balance: " + student.getOutstandingBalanceFormatted());
        LOGGER.info("Enrollment Status: " + student.getEnrollmentStatus().getDisplayName());
        LOGGER.info("Status Description: " + student.getEnrollmentStatus().getDescription());

        if (studentEnrollment != null) {
            LOGGER.info("Enrollment Date: " + studentEnrollment.getEnrollmentDate());
        }
        LOGGER.info("");
    }

    private void handleEnrollment(Student student) throws AlreadyEnrolledException {
        if (student.isEnrolled()) {
            throw new AlreadyEnrolledException("Student is already enrolled in '"
                    + student.getEnrolledProgram().getName() + "'. Cannot enroll in multiple programs simultaneously.");
        }
        showAvailablePrograms(student);
    }

    private void handlePayment(Student student) throws StudentNotEnrolledException, InvalidPaymentException {
        if (!student.isEnrolled()) {
            throw new StudentNotEnrolledException(
                    "Student must be enrolled in a program to make a payment. Please enroll in a program first.");
        }

        if (student.getOutstandingBalance() <= 0.0) {
            LOGGER.info("There is no outstanding balance to pay.");
            LOGGER.info("");
            return;
        }

        double payment = getIntInput("Enter payment amount: ");

        if (payment <= 0) {
            throw new InvalidPaymentException("Payment must be greater than zero.");
        }

        if (payment > student.getOutstandingBalance()) {
            throw new InvalidPaymentException("Payment amount ($" + String.format("%.2f", payment)
                    + ") exceeds outstanding balance ($" + String.format("%.2f", student.getOutstandingBalance())
                    + "). Please enter a valid payment amount.");
        }

        student.makePayment(payment);
        LOGGER.info("Payment recorded. New balance: " + student.getOutstandingBalanceFormatted());
        if (student.getOutstandingBalance() == 0.0) {
            LOGGER.info("Congratulations! You have fully paid your program. Best of luck with your studies!");
        }
        LOGGER.info("");
    }

    private void showAvailablePrograms(Student student) {
        Map<Department, List<Program>> availablePrograms = university.getProgramCatalog().stream()
                .collect(Collectors.groupingBy(Program::getDepartment));

        List<Program> programsList = new ArrayList<>();
        AtomicInteger programsOrder = new AtomicInteger(1);

        LOGGER.info("=== Available Programs ===");

        availablePrograms.entrySet().forEach(entry -> {
            LOGGER.info("Department: " + entry.getKey().getName() + " (" + entry.getKey().getDepartmentCode() + ")");
            entry.getValue().forEach(program -> {
                LOGGER.info(programsOrder.get() + ". " + program.getName());
                programsList.add(program);
                programsOrder.incrementAndGet();
            });
            LOGGER.info("");
        });

        while (true) {
            LOGGER.info("Enter zero (0) to go back or...");
            int option = getIntInput("Select the program you want to enroll in: ");

            if (option == 0) {
                break;
            }

            if (option >= 1 && option <= programsList.size()) {
                Program selectedProgram = programsList.get(option - 1);

                Enrollment enrollment = new Enrollment(student, selectedProgram);
                university.addEnrollment(enrollment);

                student.enroll(selectedProgram);

                LOGGER.info("Congrats! You have enrolled in " + selectedProgram.getName());
                LOGGER.info("Enrollment Date: " + enrollment.getEnrollmentDate());
                LOGGER.info("Outstanding Balance: " + student.getOutstandingBalanceFormatted());
                LOGGER.info("");
                break;
            } else {
                LOGGER.info("Invalid selection. Please try again.");
            }
        }
    }

    private void showAvailableProgramsAsGuest() {
        Map<Department, List<Program>> availablePrograms = university.getProgramCatalog().stream()
                .collect(Collectors.groupingBy(Program::getDepartment));

        LOGGER.info("=== Available Programs ===");
        LOGGER.info("");

        availablePrograms.entrySet().forEach(entry -> {
            LOGGER.info("Department: " + entry.getKey().getName() + " (" + entry.getKey().getDepartmentCode() + ")");
            entry.getValue().forEach(program -> {
                LOGGER.info(program.toString());
            });
            LOGGER.info("");
        });

        getIntInput("Enter zero (0) to go back: ");
    }

    private void showProfessorsAndCourses() {
        LOGGER.info("=== Professors ===");

        university.getProfessorRegistry().forEach(professor -> {
            LOGGER.info(professor.toString());
            if (!professor.getAssignedCourses().isEmpty()) {
                LOGGER.info("  Assigned Courses:");
                professor.getAssignedCourses().forEach(c -> {
                    LOGGER.info("   - " + c.getCourseName() + " (" + c.getCourseCode() + ") - "
                            + c.getDifficulty().getDisplayName());
                });
            }
        });

        LOGGER.info("");
        LOGGER.info("=== Student Enrollment Statistics ===");

        Map<EnrollmentStatus, List<Student>> studentsByStatus = university.getStudentRegistry().stream()
                .collect(Collectors.groupingBy(Student::getEnrollmentStatus));

        java.util.Arrays.stream(EnrollmentStatus.values()).forEach(status -> {
            List<Student> studentsWithStatus = studentsByStatus.getOrDefault(status, new ArrayList<>());
            LOGGER.info(String.format("%s Students: %d (%s)",
                    status.getDisplayName(),
                    studentsWithStatus.size(),
                    status.getDescription()));

            if (!studentsWithStatus.isEmpty() && status == EnrollmentStatus.ACTIVE) {
                LOGGER.info("  Recently Enrolled Students:");
                studentsWithStatus.stream()
                        .filter(Student::isEnrolled)
                        .limit(3)
                        .forEach(student -> LOGGER.info(
                                "   - " + student.getFullName() + " in " + student.getEnrolledProgram().getName()));
            }
        });

        LOGGER.info("=== Popular Courses by Difficulty ===");

        Map<CourseDifficulty, List<Course>> coursesByDifficulty = university.getCourseCatalog().stream()
                .collect(Collectors.groupingBy(Course::getDifficulty));

        java.util.Arrays.stream(CourseDifficulty.values()).forEach(difficulty -> {
            List<Course> coursesAtLevel = coursesByDifficulty.getOrDefault(difficulty, new ArrayList<>());
            if (!coursesAtLevel.isEmpty()) {
                LOGGER.info(String.format("--- %s Level Courses (Level %d) ---",
                        difficulty.getDisplayName(), difficulty.getLevel()));
                coursesAtLevel.forEach(course -> {
                    LOGGER.info("  " + course.toString());
                });
                LOGGER.info("");
            }
        });

        LOGGER.info("=== All Courses ===");
        university.getCourseCatalog().forEach(course -> {
            LOGGER.info(course.toString());
        });

        LOGGER.info("");
        LOGGER.info("=== Classrooms ===");

        university.getClassrooms().forEach(classroom -> {
            LOGGER.info(classroom.toString());
        });

        LOGGER.info("");
        getIntInput("Enter zero (0) to go back: ");
    }
}

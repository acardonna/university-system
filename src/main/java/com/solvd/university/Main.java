package com.solvd.university;

import com.solvd.university.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

//        University university = new University("Oxford");
//
//        initializeProgramCatalog(university);
//        initializeProfessors(university);
//        initializeCourses(university);
//        initializeClassrooms(university);
//
//        UserInterface userInterface = new UserInterface(scanner, university);
//        userInterface.start();

        workWithFiles(scanner);
    }

    private static void workWithFiles(Scanner scanner) {
        try {
            List<String> text = FileUtils.readLines(new File("src/main/resources/input.txt"), StandardCharsets.UTF_8);

            while (true) {
                System.out.print("Enter the word to find (or 0 to exit): ");
                String wordToFind = scanner.nextLine();

                if ("0".equals(wordToFind)) {
                    break;
                }

                int count = 0;

                for (String line : text) {
                    String[] words = StringUtils.split(line);
                    for (String word : words) {
                        String strippedWord = StringUtils.strip(word, ".,;:!?\"'()[]{}");
                        if (StringUtils.equalsIgnoreCase(strippedWord, wordToFind)) {
                            count++;
                        }
                    }
                }

                String message = String.format("%s: %d%n", wordToFind, count);
                FileUtils.writeStringToFile(new File("src/main/resources/output.txt"), message, StandardCharsets.UTF_8, true);

                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void initializeProgramCatalog(University university) {
        Department<String> computerScience = new ComputerScienceDepartment();
        Department<String> business = new BusinessDepartment();
        Department<String> engineering = new EngineeringDepartment();
        Department<String> mathematics = new MathematicsDepartment();
        Department<String> arts = new ArtsDepartment();

        university.getProgramCatalog().add(new Program("Bachelor of Computer Science", 4, 40000.0, computerScience));
        university.getProgramCatalog().add(new Program("Master of Software Engineering", 2, 30000.0, computerScience));
        university.getProgramCatalog().add(new Program("Bachelor of Information Technology", 4, 38000.0, computerScience));
        university.getProgramCatalog().add(new Program("Master of Data Science", 2, 35000.0, computerScience));

        university.getProgramCatalog().add(new Program("Bachelor of Business Administration", 4, 35000.0, business));
        university.getProgramCatalog().add(new Program("Master of Business Administration", 2, 45000.0, business));
        university.getProgramCatalog().add(new Program("Bachelor of Finance", 4, 37000.0, business));
        university.getProgramCatalog().add(new Program("Master of Marketing", 2, 32000.0, business));

        university.getProgramCatalog().add(new Program("Bachelor of Civil Engineering", 4, 42000.0, engineering));
        university.getProgramCatalog().add(new Program("Bachelor of Mechanical Engineering", 4, 42000.0, engineering));
        university.getProgramCatalog().add(new Program("Bachelor of Electrical Engineering", 4, 43000.0, engineering));
        university.getProgramCatalog().add(new Program("Master of Engineering Management", 2, 38000.0, engineering));

        university.getProgramCatalog().add(new Program("Bachelor of Mathematics", 4, 33000.0, mathematics));
        university.getProgramCatalog().add(new Program("Bachelor of Applied Mathematics", 4, 35000.0, mathematics));
        university.getProgramCatalog().add(new Program("Master of Statistics", 2, 28000.0, mathematics));
        university.getProgramCatalog().add(new Program("Master of Pure Mathematics", 2, 26000.0, mathematics));

        university.getProgramCatalog().add(new Program("Bachelor of Fine Arts", 4, 29000.0, arts));
        university.getProgramCatalog().add(new Program("Bachelor of English Literature", 4, 27000.0, arts));
        university.getProgramCatalog().add(new Program("Master of Creative Writing", 2, 24000.0, arts));
        university.getProgramCatalog().add(new Program("Master of Art History", 2, 25000.0, arts));
    }

    private static void initializeProfessors(University university) {
        Department<String> computerScience = new ComputerScienceDepartment();
        Department<String> business = new BusinessDepartment();
        Department<String> engineering = new EngineeringDepartment();
        Department<String> mathematics = new MathematicsDepartment();
        Department<String> arts = new ArtsDepartment();

        university.addProfessor(new Professor("John", "Smith", computerScience, "Professor"));
        university.addProfessor(new Professor("Sarah", "Johnson", business, "Associate Professor"));
        university.addProfessor(new Professor("Michael", "Brown", engineering, "Assistant Professor"));
        university.addProfessor(new Professor("Emily", "Davis", mathematics, "Professor"));
        university.addProfessor(new Professor("David", "Wilson", arts, "Associate Professor"));
    }

    private static void initializeCourses(University university) {
        Professor johnSmith = university.getProfessorRegistry().get(0);
        Professor sarahJohnson = university.getProfessorRegistry().get(1);

        Course<String, ComputerScienceDepartment> introductionToProgramming = new Course<>(101, "Introduction to Programming", 3, johnSmith, new ComputerScienceDepartment(), CourseDifficulty.INTRODUCTORY);
        Course<String, ComputerScienceDepartment> dataStructures = new Course<>(201, "Data Structures", 4, johnSmith, new ComputerScienceDepartment(), CourseDifficulty.INTERMEDIATE);
        Course<String, BusinessDepartment> businessFundamentals = new Course<>(101, "Business Fundamentals", 3, sarahJohnson, new BusinessDepartment(), CourseDifficulty.INTRODUCTORY);

        johnSmith.assignCourse(introductionToProgramming);
        johnSmith.assignCourse(dataStructures);
        sarahJohnson.assignCourse(businessFundamentals);

        if (!university.getClassrooms().isEmpty()) {
            Classroom classroom1 = university.getClassrooms().get(0);
            Classroom classroom2 = university.getClassrooms().get(1);
            introductionToProgramming.schedule(
                    java.time.LocalDateTime.now().plusDays(1),
                    java.time.LocalDateTime.now().plusDays(1).plusHours(2),
                    classroom1);
            dataStructures.schedule(
                    java.time.LocalDateTime.now().plusDays(2),
                    java.time.LocalDateTime.now().plusDays(2).plusHours(2),
                    classroom2);
        }

        university.addCourse(introductionToProgramming);
        university.addCourse(dataStructures);
        university.addCourse(businessFundamentals);
    }

    private static void initializeClassrooms(University university) {
        university.addClassroom(new Classroom("101", new Building("Science Building"), 30, "Computer Lab"));
        university.addClassroom(new Classroom("205", new Building("Main Hall"), 50, "Lecture Hall"));
        university.addClassroom(new Classroom("301", new Building("Engineering Building"), 25, "Laboratory"));
        university.addClassroom(new Classroom("150", new Building("Business Center"), 40, "Standard"));
    }
}
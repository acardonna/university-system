package com.solvd.university.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

import com.solvd.university.model.annotation.RequiredExperience;

@RequiredExperience(level = 2)
public class Course<T, D extends Department<T>> implements Identifiable, Schedulable {

    private String courseCode;
    private String courseName;
    private int creditHours;
    private Professor professor;
    private D department;
    private final String id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Classroom classroom;
    private List<Grade<Double>> courseGrades;
    private TreeMap<LocalDateTime, Grade<Double>> gradesByDate;
    private CourseDifficulty difficulty;

    public Course() {
        this.id = "COURSE-UNASSIGNED";
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = CourseDifficulty.INTRODUCTORY;
    }

    public Course(String courseCode, String courseName, int creditHours, Professor professor, D department) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.professor = professor;
        this.department = department;
        this.id = courseCode;
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = CourseDifficulty.INTRODUCTORY;
    }

    public Course(int courseNumber, String courseName, int creditHours, Professor professor, D department) {
        this.courseCode = department.getDepartmentCode() + String.valueOf(courseNumber);
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.professor = professor;
        this.department = department;
        this.id = this.courseCode;
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = CourseDifficulty.INTRODUCTORY;
    }

    public Course(String courseCode, String courseName, int creditHours, Professor professor, D department,
            CourseDifficulty difficulty) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.professor = professor;
        this.department = department;
        this.id = courseCode;
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = difficulty;
    }

    public Course(int courseNumber, String courseName, int creditHours, Professor professor, D department,
            CourseDifficulty difficulty) {
        this.courseCode = department.getDepartmentCode() + String.valueOf(courseNumber);
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.professor = professor;
        this.department = department;
        this.id = this.courseCode;
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = difficulty;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public D getDepartment() {
        return department;
    }

    public void setDepartment(D department) {
        this.department = department;
    }

    public CourseDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(CourseDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getFormattedCourseCode() {
        return String.format("%s (%s)", courseCode, department.getDepartmentCode());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void schedule(LocalDateTime start, LocalDateTime end, Classroom room) {
        this.start = start;
        this.end = end;
        this.classroom = room;
    }

    public void addGrade(Grade<Double> grade) {
        courseGrades.add(grade);
        gradesByDate.put(LocalDateTime.now(), grade);
    }

    @RequiredExperience(level = 3)
    public void addGrade(Grade<Double> grade, GradeValidator validator) {
        if (validator.isValid(grade.value())) {
            courseGrades.add(grade);
            gradesByDate.put(LocalDateTime.now(), grade);
        } else {
            throw new IllegalArgumentException("Invalid grade value: " + grade.value());
        }
    }

    public void addGradeWithDate(Grade<Double> grade, LocalDateTime date) {
        courseGrades.add(grade);
        gradesByDate.put(date, grade);
    }

    public void addGradeWithDate(Grade<Double> grade, LocalDateTime date, GradeValidator validator) {
        if (validator.isValid(grade.value())) {
            courseGrades.add(grade);
            gradesByDate.put(date, grade);
        } else {
            throw new IllegalArgumentException("Invalid grade value: " + grade.value());
        }
    }

    public List<Grade<Double>> getCourseGrades() {
        return new ArrayList<>(courseGrades);
    }

    public double calculateCourseAverage() {
        if (courseGrades.isEmpty()) {
            return 0.0;
        }
        return courseGrades.stream()
                .mapToDouble(Grade::value)
                .average()
                .orElse(0.0);
    }

    public List<Grade<Double>> getGradesForSemester(String semester) {
        return courseGrades.stream()
                .filter(grade -> grade.semester().equals(semester))
                .toList();
    }

    public NavigableMap<LocalDateTime, Grade<Double>> getGradesByDate() {
        return new TreeMap<>(gradesByDate);
    }

    public Grade<Double> getLatestGrade() {
        return gradesByDate.isEmpty() ? null : gradesByDate.lastEntry().getValue();
    }

    public Grade<Double> getEarliestGrade() {
        return gradesByDate.isEmpty() ? null : gradesByDate.firstEntry().getValue();
    }

    public Map<LocalDateTime, Grade<Double>> getGradesInDateRange(LocalDateTime start, LocalDateTime end) {
        return gradesByDate.subMap(start, true, end, true);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course<?, ?> course = (Course<?, ?>) o;
        return creditHours == course.creditHours && Objects.equals(courseCode, course.courseCode)
                && Objects.equals(courseName, course.courseName) && Objects.equals(professor, course.professor)
                && Objects.equals(department, course.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, courseName, creditHours, professor, department);
    }

    @Override
    public String toString() {
        String sched = (start != null && end != null && classroom != null)
                ? String.format(" | %s to %s in %s %s", start, end, classroom.getBuilding().getName(),
                        classroom.getRoomNumber())
                : "";
        String gradeInfo = courseGrades.isEmpty()
                ? ""
                : String.format(" | Avg Grade: %.1f", calculateCourseAverage());
        String difficultyInfo = (difficulty != null)
                ? String.format(" | Difficulty: %s", difficulty.getDisplayName())
                : "";
        return String.format("%s - %s | %d Credit Hours | Professor: %s | Department: %s%s%s%s",
                getFormattedCourseCode(), courseName, creditHours, professor.getFullName(), department.getName(),
                difficultyInfo, sched, gradeInfo);
    }
}

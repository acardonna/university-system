package com.solvd.university.model.annotation;

/**
 * This is for working with @RequiredExperience annotation using
 * reflection
 */
public class ExperienceAnalyzer {

    /**
     * Checks if a class has the @RequiredExperience annotation
     */
    public static boolean hasRequirement(Class<?> clazz) {
        return clazz.isAnnotationPresent(RequiredExperience.class);
    }

    /**
     * Gets the required level from a class annotation
     */
    public static int getRequiredLevel(Class<?> clazz) {
        if (hasRequirement(clazz)) {
            RequiredExperience annotation = clazz.getAnnotation(RequiredExperience.class);
            return annotation.level();
        }
        return 1;
    }

    /**
     * Checks if a student meets the level requirement for a class
     */
    public static boolean meetsRequirement(Object student, Class<?> targetClass) {
        int requiredLevel = getRequiredLevel(targetClass);
        int studentLevel = getStudentLevel(student);
        return studentLevel >= requiredLevel;
    }

    /**
     * Gets student's academic level using reflection
     */
    private static int getStudentLevel(Object student) {
        try {
            var getGradeLevel = student.getClass().getMethod("getGradeLevel");
            var gradeLevel = getGradeLevel.invoke(student);
            var getYear = gradeLevel.getClass().getMethod("getYear");
            return (Integer) getYear.invoke(gradeLevel);
        } catch (Exception e) {
            return 1;
        }
    }
}

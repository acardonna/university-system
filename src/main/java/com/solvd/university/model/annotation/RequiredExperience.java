package com.solvd.university.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark the required academic level for university elements
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface RequiredExperience {
    /**
     * The required academic level (1-5, where 1=Freshman, 5=Graduate)
     */
    int level() default 1;
}

package com.greenpepper.annotation;

import java.lang.annotation.*;

/**
 * Indicate that a Method is a {@link com.greenpepper.reflect.CollectionProvider}.
 * This will only be used for documentation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface FixtureCollection {

    boolean deprecated() default false;

    boolean bestPractice() default true;

}

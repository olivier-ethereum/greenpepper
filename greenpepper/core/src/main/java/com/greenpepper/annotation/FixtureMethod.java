package com.greenpepper.annotation;

import java.lang.annotation.*;

/**
 * Indicate that a method is a Fixture method (used in GreenPepper). This will only be used for
 * documentation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface FixtureMethod {

    /**
     * Get the description of this Fixture
     * @return the description
     */
    String value() default "";

    boolean bestPractice() default true;

    boolean deprecated() default false;

    String replacedWith() default "";

    String usage() default "";
}

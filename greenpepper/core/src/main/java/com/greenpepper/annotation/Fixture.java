package com.greenpepper.annotation;

import java.lang.annotation.*;

/**
 * Indicate that a Class is a Fixture. This will only be used for documentation. So the retention is
 * only Source leveled.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Fixture {

    /**
     * Get the description of this Fixture
     * @return the description
     */
    String value() default "";

    /**
     * Get the list of Fixtures that can be somehow related to this one.
     * @return the list of Fixture.
     */
    Class<?>[] relatedTo() default {};

    /**
     * The category of this fixture.
     * @return
     */
    String category() default "";

    boolean obsolete() default false;

    boolean validate() default true;

    String usage() default "";

    boolean correctionPrioritaire() default false;

    boolean isAbstract() default false;
}

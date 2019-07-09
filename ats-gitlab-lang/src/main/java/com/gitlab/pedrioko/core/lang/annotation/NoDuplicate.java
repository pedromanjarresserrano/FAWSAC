package com.gitlab.pedrioko.core.lang.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicate {

    String value() default "";

    String[] values() default {};
}

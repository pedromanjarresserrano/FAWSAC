package com.gitlab.pedrioko.core.lang.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AlphabetSearch {

    String field() default "";

}

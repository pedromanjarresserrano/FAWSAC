package com.gitlab.pedrioko.core.view.api;

import java.lang.reflect.Field;

public interface TransformerAnnotation {

    Object Validate(Field f, Object value);
}

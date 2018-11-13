package com.gitlab.pedrioko.core.view.api;

import java.lang.reflect.Field;

public interface ValidateAnnotation {
    void Validate(Field f, Object value);
}

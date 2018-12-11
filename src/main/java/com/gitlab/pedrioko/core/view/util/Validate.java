package com.gitlab.pedrioko.core.view.util;

import com.gitlab.pedrioko.core.lang.annotation.GridViewFieldName;
import com.gitlab.pedrioko.core.lang.annotation.GridViewFieldPreview;
import com.gitlab.pedrioko.core.lang.annotation.NoDuplicate;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Validate {

    public static boolean noDuplicate(Object val, List<?> list) {
        if (val.getClass().isAnnotationPresent(NoDuplicate.class)) {
            String split = getNoDuplicateValue(val);
            if (split.isEmpty())
                return false;
            Object valueFieldObject = ReflectionJavaUtil.getValueFieldObject(split, val);
            return !list.stream().filter(e -> ReflectionJavaUtil.getValueFieldObject(split, e).equals(valueFieldObject))
                    .collect(Collectors.toList()).isEmpty();
        } else {
            return false;
        }

    }

    private static Annotation getAnnotation(Object val, Class class1) {
        return val.getClass().getAnnotation(class1);
    }

    private static Annotation getAnnotation(Class getClass1, Class AnotateClass) {
        return getClass1.getAnnotation(AnotateClass);
    }

    public static String getNoDuplicateValue(Object val) {
        return ((NoDuplicate) getAnnotation(val, NoDuplicate.class)).value();
    }

    public static String getGridViewFieldName(Object val) {
        return getGridViewFieldName(val.getClass());
    }

    private static String getGridViewFieldName(Class val) {
        return ((GridViewFieldName) getAnnotation(val, GridViewFieldName.class)).value();
    }

    public static Map<String, Object> getGridViewFieldPreview(Object val) {
        return getGridViewFieldPreview(val.getClass());
    }

    private static Map<String, Object> getGridViewFieldPreview(Class val) {
        GridViewFieldPreview annotation = (GridViewFieldPreview) getAnnotation(val, GridViewFieldPreview.class);
        HashMap map = new HashMap();
        map.put("value", annotation.value());
        map.put("isList", annotation.isList());
        map.put("replaceValue", annotation.replaceValue());

        return map;
    }
}

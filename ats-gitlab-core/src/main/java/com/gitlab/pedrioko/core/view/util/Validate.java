package com.gitlab.pedrioko.core.view.util;

import com.gitlab.pedrioko.core.lang.annotation.NoDuplicate;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.services.CrudService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Validate {

    public static boolean noDuplicate(Object val, List<?> list) {
        if (val.getClass().isAnnotationPresent(NoDuplicate.class)) {
            CrudService bean = ApplicationContextUtils.getBean(CrudService.class);
            PathBuilder<?> pathBuilder = bean.getPathBuilder(val.getClass());
            List<String> noDuplicateValues = getNoDuplicateValues(val);
            if (noDuplicateValues == null || noDuplicateValues.isEmpty()) {
                String split = getNoDuplicateValue(val);
                if (split.isEmpty())
                    return false;

                Object valueFieldObject = ReflectionJavaUtil.getValueFieldObject(split, val);
                BooleanExpression eq = pathBuilder.get(split).eq(valueFieldObject);

                return bean.query().from(pathBuilder).where(eq).fetchCount() > 0;
            } else {
                Predicate where = null;
                for (String e : noDuplicateValues) {
                    Object valueFieldObject = ReflectionJavaUtil.getValueFieldObject(e, val);
                    BooleanExpression eq = pathBuilder.get(e).eq(valueFieldObject);
                    if (where == null)
                        where = eq;
                    else {
                        where = ((BooleanExpression) where).or(eq);
                    }
                }
                return bean.query().from(pathBuilder).where(where).fetchCount() > 0;

            }
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

    public static List<String> getNoDuplicateValues(Object val) {
        return Arrays.asList(((NoDuplicate) getAnnotation(val, NoDuplicate.class)).values());
    }

    public static String getGridViewFieldName(Object val) {
        return getGridViewFieldName(val.getClass());
    }


    public static Map<String, Object> getGridViewFieldPreview(Object val) {
        return getGridViewFieldPreview(val.getClass());
    }

}

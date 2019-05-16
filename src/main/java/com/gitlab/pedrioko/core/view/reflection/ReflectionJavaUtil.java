package com.gitlab.pedrioko.core.view.reflection;

import com.gitlab.pedrioko.core.view.reflection.enums.ClassMethod;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Id;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionJavaUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionJavaUtil.class);

    public static Object getIdValue(Object obj) {
        if (obj == null) return obj;
        List<Field> fields = getFields(obj.getClass());
        Optional<Field> optional = fields.stream().filter(e -> e.isAnnotationPresent(Id.class)).findFirst();
        return optional.isPresent() ? getValueFieldObject(optional.get().getName(), obj) : null;
    }

    public static Object getVersionValue(Object obj) {
        List<Field> fields = getFields(obj.getClass());
        Optional<Field> optional = fields.stream().filter(e -> e.isAnnotationPresent(Version.class)).findFirst();
        return optional.isPresent() ? getValueFieldObject(optional.get().getName(), obj) : null;
    }

    public static boolean newsIsUpdate(Object news, Object old) {
        if (getIdValue(news).equals(getIdValue(old))) {
            throw new IllegalArgumentException("Different Id value");
        }
        return Long.parseLong(getVersionValue(news).toString()) > Long.parseLong(getVersionValue(old).toString());
    }

    public static void removeById(List<?> list, Object id) {
        List aux = new ArrayList(list);
        for (Object e : aux) {
            if (getIdValue(e).toString().equalsIgnoreCase(id.toString())) {
                list.remove(e);
                break;
            }
        }

    }

    public static Object getValueFieldObject(String fieldname, Object obj) {
        if (obj != null) {
            try {
                return PropertyUtils.getProperty(obj, fieldname);
            } catch (IllegalAccessException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.error("ERROR on getValueFieldObject() - " + obj.getClass(), e);
            }
        }
        return null;
    }

    public static void setValueFieldObject(String fieldname, Object obj, Object value) {
        try {
            PropertyUtils.setProperty(obj, fieldname, value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error("ERROR on setValueFieldObject()", e);
        }
    }

    public static String getNameMethod(Field k, ClassMethod metodo) {
        return getNameMethod(k.getName(), metodo);
    }

    public static String getNameMethod(String fieldname, ClassMethod metodo) {
        switch (metodo) {
            case SET:
                return "set" + StringUtils.capitalize(fieldname);
            case GET:
                return "get" + StringUtils.capitalize(fieldname);
            case IS:
                return "is" + StringUtils.capitalize(fieldname);
            default:
                return null;
        }
    }

    public static Object getNewInstace(Field k) {
        return getNewInstace(k.getType());
    }

    public static Object getNewInstace(Class<?> class1) {
        if (class1 == List.class)
            return new ArrayList<>();
        else if (class1.isEnum())
            return class1.getEnumConstants()[0];
        else
            try {
                return class1.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("ERROR on getNewInstace()", e);
            }
        return null;
    }

    public static List<Field> getStringFields(Class<?> klass) {
        return getFields(klass).stream().filter(e -> e.getType() != List.class
                && !e.getName().equalsIgnoreCase("serialVersionUID") && e.getType() == String.class)
                .collect(Collectors.toList());
    }

    public static List<String> getFieldsNames(Class<?> klass) {
        List<Field> fields = getFields(klass);
        return fields.stream().map(Field::getName).collect(Collectors.toList());
    }

    public static List<Field> getFields(Class<?> klass) {
        return FieldUtils.getAllFieldsList(klass)
                .stream()
                .filter(e -> !e.getName().equalsIgnoreCase("serialVersionUID"))
                .collect(Collectors.toList());
    }


    public static List<Field> getCollectionsFields(Class<?> klass) {
        return getFieldsByType(klass, Collection.class);
    }

    public static List<Field> getFieldsByType(Class<?> klass, Class<?> type) {
        return FieldUtils.getAllFieldsList(klass)
                .stream()
                .filter(e -> !e.getName().equalsIgnoreCase("serialVersionUID"))
                .filter(e -> type.isAssignableFrom(e.getType()))
                .collect(Collectors.toList());
    }

    public static Field getField(Class<?> klass, String field) {
        try {
            return ReflectionUtils.findField(klass, field);
        } catch (SecurityException e) {
            LOGGER.error("ERROR on getField()", e);
            return null;
        }
    }

    public static boolean compareEqual(Class<?> k, Object x1, Object x2) {
        if (x1.getClass() != x2.getClass())
            return false;
        for (Field f : getFields(k)) {
            String fieldname = f.getName();
            if (!String.valueOf(getValueFieldObject(fieldname, x1)).trim()
                    .equalsIgnoreCase(String.valueOf(getValueFieldObject(fieldname, x2)).trim()))
                return false;

        }
        return true;
    }

}

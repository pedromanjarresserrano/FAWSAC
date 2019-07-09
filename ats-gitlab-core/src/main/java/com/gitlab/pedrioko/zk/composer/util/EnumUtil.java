package com.gitlab.pedrioko.zk.composer.util;

import org.springframework.stereotype.Component;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The Class EnumUtil.
 */
@Component("enumutil")
public class EnumUtil {

    /**
     * Enum values.
     *
     * @param <T>      the generic type
     * @param enumType the enum type
     * @return the list
     */
    public <T extends Enum<T>> List<String> enumValues(Class<T> enumType) {
        List<String> lista = new ArrayList<>();
        for (T c : enumType.getEnumConstants()) {
            lista.add(c.name());
        }
        return lista;
    }

    /**
     * Gets the enum of value.
     *
     * @param <T>       the generic type
     * @param enumvalue the enumvalue
     * @param enumclass the enumclass
     * @return the enum of value
     */
    public <T extends Enum<T>> T getEnumOfValue(String enumvalue, Class<T> enumclass) {
        for (T c : enumclass.getEnumConstants()) {
            if (enumvalue.equals(c.name())) {
                return c;
            }
        }

        return null;
    }

    /**
     * Gets the enum by index.
     *
     * @param <T>  the generic type
     * @param type the type
     * @param c    the c
     * @return the enum by index
     */
    public <T> T getEnumByIndex(Integer type, Class<T> c) {
        return c.getEnumConstants()[type];
    }

    /**
     * Gets the index gender.
     *
     * @param <T> the generic type
     * @param c   the c
     * @param o   the o
     * @return the index gender
     */
    public <T> Integer getIndexEnum(Class<T> c, Object o) {
        T[] t = c.getEnumConstants();
        for (int i = 0; i < t.length; i++) {
            if (t[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the label of enum.
     *
     * @param base the base
     * @param c    the c
     * @param o    the o
     * @return the label of enum
     */
    public String getLabelOfEnum(String base, Class<?> c, Object o) {
        StringBuilder aux = new StringBuilder();
        Arrays.stream(c.getEnumConstants()).forEach(e -> {
            if (e.equals(o)) {
                aux.append(Labels.getLabel(base + e.toString().toLowerCase()));
            }
        });
        return aux.toString();
    }

    /**
     * Gets the labels of enum.
     *
     * @param base the base
     * @param c    the c
     * @return the labels of enum
     */
    public List<String> getLabelsOfEnum(String base, Class<?> c) {
        List<String> lista = new ArrayList<>();
        Arrays.stream(c.getEnumConstants()).forEach(e -> lista.add(Labels.getLabel(base + e.toString().toLowerCase())));
        return lista;
    }

    /**
     * Gets the labels of enum.
     *
     * @param <T>  the generic type
     * @param base the base
     * @param c    the c
     * @return the labels of enum
     */
    public <T> String getLabelsOfEnum(String base, T c) {
        return Labels.getLabel(base + c.toString().toLowerCase());
    }

    /**
     * Gets the labels of enum.
     *
     * @param base    the base
     * @param c       the c
     * @param keylang the keylang
     * @return the labels of enum
     */
    public List<String> getLabelsOfEnum(String base, Class<?> c, String keylang) {
        List<String> lista = new ArrayList<>();
        Locale local = Locales.getCurrent();
        Locales.setThreadLocal(new Locale(keylang));
        Arrays.stream(c.getEnumConstants()).forEach(e -> lista.add(Labels.getLabel(base + e.toString().toLowerCase())));
        Locales.setThreadLocal(local);
        return lista;
    }

    /**
     * Gets the enum of label.
     *
     * @param <T>   the generic type
     * @param base  the base
     * @param label the label
     * @param c     the c
     * @return the enum of label
     */
    public <T extends Enum<T>> T getEnumOfLabel(String base, String label, Class<T> c) {
        for (T e : c.getEnumConstants()) {
            String aux = Labels.getLabel(base + e.toString().toLowerCase());
            if (label.equals(aux)) {
                return e;
            }
        }
        return null;
    }
}

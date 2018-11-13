package com.gitlab.pedrioko.core.view.util;

import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class ArraysUtil.
 */
public class ArraysUtil {

    public static void removeDuplicates(Class<?> typeClass, List<? extends Object> fulllist, List<?> toremove) {

        if (toremove != null)
            if (!toremove.isEmpty()) {
                List<? extends Object> aux = new ArrayList<>(fulllist);
                for (int i = 0; i < fulllist.size(); i++) {
                    for (int x = 0; x < toremove.size(); x++) {
                        if (String.valueOf(ReflectionJavaUtil.getIdValue(toremove.get(x))).trim().equalsIgnoreCase(
                                String.valueOf(ReflectionJavaUtil.getIdValue(fulllist.get(i))).trim()))
                            aux.remove(fulllist.get(i));
                    }
                }
                fulllist.clear();
                fulllist.addAll((List) aux);
            }
    }

    /**
     * Suma.
     *
     * @param list the list
     * @return the int
     */
    public static int suma(List<Long> list) {
        return list.parallelStream().mapToLong(Long::longValue).mapToObj(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue();
    }

    /**
     * Removes the duplicates.
     *
     * @param <T>  the generic type
     * @param list the list
     * @return the list
     */
    public static <T> List<T> removeDuplicates(List<T> list) {
        return list.stream().distinct().collect(Collectors.toList());

    }

    /**
     * Promedio.
     *
     * @param list the list
     * @return the int
     */
    public static double promedio(List<Long> list) {
        return list.stream().mapToDouble(a -> a).average().orElse(0);
    }

}

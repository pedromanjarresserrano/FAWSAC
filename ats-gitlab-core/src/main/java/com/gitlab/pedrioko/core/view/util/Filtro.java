package com.gitlab.pedrioko.core.view.util;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The Class Filtro.
 */
@Service("filtro")
public class Filtro {

    /**
     * Filter.
     *
     * @param <T>       the generic type
     * @param source    the source
     * @param predicate the predicate
     * @return the list
     */
    public <T> List<T> filter(Collection<? extends T> source, Predicate<? super T> predicate) {
        return source.stream().filter(predicate).collect(Collectors.toList());
    }

}

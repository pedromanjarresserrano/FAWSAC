package com.gitlab.pedrioko.domain.util;

import com.gitlab.pedrioko.domain.enumdomain.Idioma;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The Class IdiomaAux.
 */
@AllArgsConstructor
public @Data
class IdiomaAux {

    /**
     * The idioma.
     */
    private Idioma idioma;

    /**
     * The label.
     */
    private String label;

}

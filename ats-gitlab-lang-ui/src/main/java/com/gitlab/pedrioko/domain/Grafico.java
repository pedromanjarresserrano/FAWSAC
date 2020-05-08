package com.gitlab.pedrioko.domain;

import com.gitlab.pedrioko.core.lang.BaseEntity;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.domain.enumdomain.TipoChart;
import lombok.Data;

import javax.persistence.*;

@Entity
public @Data
class Grafico extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    @NoEmpty
    private String columnaetiquetas;
    @Lob
    @NoEmpty
    private String columnavalores;
    @Enumerated(EnumType.STRING)
    private TipoChart tipo;

}

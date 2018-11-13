package com.gitlab.pedrioko.domain;

import com.gitlab.pedrioko.core.lang.annotation.NoDuplicate;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.core.lang.annotation.TextArea;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoDuplicate(value = "SQLquery")
public @Data
class Reporte implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @NoEmpty
    private String nombre;
    @ElementCollection
    @NoEmpty
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> columnas;
    @Lob
    @TextArea
    private String descripcion;
    @TextArea
    @NoEmpty
    private String SQLquery;
    private Boolean usaRangoFecha = Boolean.FALSE;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Grafico> graficos = new ArrayList<>();

    @Version
    private int version;
}

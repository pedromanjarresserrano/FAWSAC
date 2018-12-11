package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.annotation.NoDuplicate;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoDuplicate(values = {"nombre"})
public @Data
class Report implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @NoEmpty
    private String nombre;
    @NoEmpty
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private FileEntity jasperFile;
    @Version
    private int version;
}

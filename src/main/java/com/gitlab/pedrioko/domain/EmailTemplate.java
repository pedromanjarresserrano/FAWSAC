package com.gitlab.pedrioko.domain;

import com.gitlab.pedrioko.core.lang.annotation.Ckeditor;
import com.gitlab.pedrioko.core.lang.annotation.NoDuplicate;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.domain.enumdomain.TipoNotificaion;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "EmailTemplate")
@NoDuplicate(value = "tipo")
@EqualsAndHashCode(callSuper = false)
public @Data
class EmailTemplate {
    /**
     * The iddiagnostico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(length = 50000)
    @NoEmpty
    private String nombre;

    @Ckeditor
    @Lob
    @NoEmpty
    private String plantilla;
    @Enumerated(EnumType.STRING)
    @NoEmpty
    private TipoNotificaion tipo;
}

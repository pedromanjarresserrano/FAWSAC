package com.gitlab.pedrioko.domain;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.lang.annotation.Ckeditor;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = false)
public @Data
class Anuncio extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private long id;
    @Lob
    @NoEmpty
    private String nombre;

    @ElementCollection(targetClass = TipoUsuario.class, fetch = FetchType.LAZY)
    @JoinTable(name = "anuncio_tipo_usuario", joinColumns = @JoinColumn(name = "id_anuncio"))
    @Column(name = "value", nullable = false)
    @Enumerated(EnumType.STRING)
    @NoEmpty
    private List<TipoUsuario> typelistuser;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NoEmpty
    private FileEntity urlbanner;

    @Ckeditor
    @Lob
    @NoEmpty
    private String content;

    private boolean activo;

}

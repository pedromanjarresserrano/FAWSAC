package com.gitlab.pedrioko.domain;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.lang.annotation.Email;
import com.gitlab.pedrioko.core.lang.annotation.NoDuplicate;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.core.lang.annotation.Password;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * The Class Usuario.
 */
@Entity
@Table(name = "Usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(callSuper = false)
@NoDuplicate(values = {"username", "email", "cedula"})
public @Data
class Usuario extends BaseEntity {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The idusuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(nullable = false)
    private long idusuario;

    /**
     * The nombres.
     */
    @Lob
    @Column(length = 50000)
    private String nombres;

    /**
     * The apellidos.
     */
    @Lob
    @Column(length = 50000)
    private String apellidos;

    /**
     * The email.
     */
    @Lob
    @Column(length = 50000)
    @Email
    @NoEmpty
    private String email;

    /**
     * The telefono.
     */
    private long telefono;

    /**
     * The cedula.
     */
    @Lob
    @Column(length = 50000)
    @NoEmpty
    private String cedula;

    /**
     * The username.
     */
    @Lob
    @Column(length = 50000)
    @NoEmpty
    private String username;

    /**
     * The password.
     */
    @Lob
    @Column(length = 50000)
    @Password
    @NoEmpty
    private String password;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private FileEntity picture;
    /**
     * The tipo.
     */
    @Enumerated(EnumType.STRING)
    @NoEmpty
    private TipoUsuario tipo;

    /**
     * The version.
     */

    /**
     * The token.
     */
    @Lob
    @Column(length = 50000)
    private String token;

    @Lob
    @Column(length = 50000)
    private String webSocketId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USR_PFS",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "idusuario"),
            inverseJoinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"))
    @NoEmpty
    private List<UserProfile> userprofiles;


    private Boolean enable = Boolean.TRUE;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return cedula + " - " + nombres + apellidos;
    }

}

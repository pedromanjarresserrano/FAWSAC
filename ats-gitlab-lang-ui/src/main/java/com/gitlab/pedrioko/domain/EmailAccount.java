package com.gitlab.pedrioko.domain;

import com.gitlab.pedrioko.core.lang.BaseEntity;
import com.gitlab.pedrioko.core.lang.annotation.NoDuplicate;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoDuplicate(value = "username")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data
class EmailAccount extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    @Column(length = 50000)
    @NoEmpty
    private String nombre;


    @Lob
    @NoEmpty
    private String host;


    @Lob
    @NoEmpty
    private String username;


    @Lob
    @NoEmpty
    private String password;


    @Lob
    @NoEmpty
    private String protocol;


    private boolean smtpauth = true;

    private boolean starttls = true;

    private boolean starttlsrequired = true;

    @NoEmpty
    private int port;

}

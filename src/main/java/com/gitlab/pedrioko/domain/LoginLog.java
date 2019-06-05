package com.gitlab.pedrioko.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public @Data
class LoginLog  extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(nullable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario user;

    @Lob
    @Column(length = 50000)
    private String ip;
}

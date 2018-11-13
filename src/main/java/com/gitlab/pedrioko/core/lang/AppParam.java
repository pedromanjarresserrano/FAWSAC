package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public @Data
class AppParam implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(nullable = false)
    private long id;
    @NoEmpty
    @Lob
    private String name;
    @NoEmpty
    @Lob
    private String value;
    @Version
    private int version;

    public AppParam(int id, String name, String value) {
        this.name = name;
        this.value = value;
        this.id = id;
    }
}

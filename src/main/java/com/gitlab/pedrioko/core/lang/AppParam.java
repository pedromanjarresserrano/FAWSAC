package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.core.lang.enums.TypeAppParam;
import com.gitlab.pedrioko.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public @Data
class AppParam extends BaseEntity {
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
    @Enumerated(EnumType.STRING)
    @NoEmpty
    private TypeAppParam typeAppParam = TypeAppParam.VARIABLE;

}

package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.annotation.NoDuplicate;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.domain.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@NoDuplicate(value = "menuprovider")
public @Data
class ProviderAccess extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @NoEmpty
    private String menuprovider;

    @ElementCollection(fetch = FetchType.EAGER)
    @NoEmpty
    private List<String> actions;

    @Version
    private int version;

    @Override
    public String toString() {
        return menuprovider;
    }

}

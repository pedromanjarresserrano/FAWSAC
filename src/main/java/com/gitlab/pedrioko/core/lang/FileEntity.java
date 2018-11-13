package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.annotation.UIEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@UIEntity
public @Data
class FileEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String url;

    private String filename;

    @Version
    private int version;
}

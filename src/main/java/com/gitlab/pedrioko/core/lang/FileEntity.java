package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.annotation.UIEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    private Date creationDate = new Date();
    @Version
    private int version;

}

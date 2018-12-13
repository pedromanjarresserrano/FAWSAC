package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
public @Data
class AuditLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(nullable = false)
    private long id;
    @Lob
    @Column(length = 50000)
    private String action;
    @Lob
    @Column(length = 50000)
    private String userid;
    @Lob
    @Column(length = 50000)
    private String user;
    @Lob
    @Column(length = 50000)
    private String detail;
    private Date createdDate;
    private long entityId;
    @Lob
    @Column(length = 50000)
    private String entityName;
    @Lob
    @Column(length = 50000)
    private String ip;
    @Version
    private int version;

    public AuditLog() {
    }
}
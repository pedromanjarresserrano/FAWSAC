package com.gitlab.pedrioko.core.lang;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    //   @CreatedBy
    //   private User createBy;

    @CreatedDate
    protected Date createdAt;

    @LastModifiedDate
    protected Date lastModified;

    @Version
    protected int version;

}
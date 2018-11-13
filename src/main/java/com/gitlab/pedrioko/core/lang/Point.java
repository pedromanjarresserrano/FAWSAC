package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.annotation.UIEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import java.io.Serializable;

@Entity
@UIEntity
public @Data
class Point implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Digits(integer = 5, fraction = 50)
    private double lat;

    @Digits(integer = 5, fraction = 50)
    private double lng;


}

package com.gitlab.pedrioko.core.hibernate.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Limit {

    private long offset;
    private long limit;

    public Limit() {
    }
}

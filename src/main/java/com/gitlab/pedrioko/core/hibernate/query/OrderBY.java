package com.gitlab.pedrioko.core.hibernate.query;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class OrderBY {

    private String field;
    private String type;

    public OrderBY() {
    }
}

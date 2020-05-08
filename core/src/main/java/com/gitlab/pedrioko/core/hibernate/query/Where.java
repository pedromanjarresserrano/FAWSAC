package com.gitlab.pedrioko.core.hibernate.query;

import lombok.Data;

@Data
public class Where {
    private String field;
    private Object value;
    private Where aditional;
    private OrderBY orderBY;
    private Limit limit;

    public Where() {
    }

    public Where(String field, String operartion, Object value) {
        this.field = field;
        this.value = value;
    }

    public Where setLimit(Limit limit) {
        this.limit = limit;
        return this;
    }

    public Where setField(String field) {
        this.field = field;
        return this;
    }


    public Where setValue(Object value) {
        this.value = value;
        return this;
    }

    public Where setAditional(Where aditional) {
        this.aditional = aditional;
        return this;
    }

    public Where setOrderBY(OrderBY orderBY) {
        this.orderBY = orderBY;
        return this;
    }
}

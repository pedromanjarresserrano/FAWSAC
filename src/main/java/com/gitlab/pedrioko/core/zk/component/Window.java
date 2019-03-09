package com.gitlab.pedrioko.core.zk.component;

public class Window extends org.zkoss.zul.Div {

    protected Object _value;


    public Object getValue() {
        return _value;
    }

    public void setValue(Object value) {
        _value = value;
        smartUpdate("value", value);
    }
}

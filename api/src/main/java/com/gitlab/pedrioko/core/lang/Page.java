package com.gitlab.pedrioko.core.lang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zkoss.zk.ui.Component;

import java.util.HashMap;
import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
public @Data
class Page {

    private Class<?> pageClass;

    private  String uriView;

    private Component component;

    HashMap<Object, Object> arg = new HashMap<>();

    public Object getArg(Object key) {
        return arg.get(key);
    }

    public Object putArg(Object key, Object value) {
        return arg.put(key, value);
    }

    public void putAllArgs(Map<?, ?> m) {
        arg.putAll(m);
    }


    public Page(Class<?> pageClass) {
        this.pageClass = pageClass;
    }

    public Page(String uriView) {
        this.uriView = uriView;
    }

    public Page(String uriView, HashMap<Object, Object> arg) {
        this.uriView = uriView;
        this.arg = arg;
    }
}

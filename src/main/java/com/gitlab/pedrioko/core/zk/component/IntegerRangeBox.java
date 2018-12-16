package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.IntegerRange;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;

public class IntegerRangeBox extends Div {
    Intbox fin = new Intbox();
    Intbox inicio = new Intbox();
    private IntegerRange value;

    public IntegerRangeBox() {
        value = new IntegerRange();
        load();
    }

    private void load() {
        inicio.setWidth("100%");
        fin.setWidth("100%");
        appendChild(inicio);
        appendChild(fin);
    }

    public IntegerRange getValue() {
        Integer inicio = this.inicio.getValue();

        Integer fin = this.fin.getValue();
        if (inicio != null && fin != null) {
            this.value.setInicio(inicio);
            this.value.setFin(fin);
            return this.value;
        } else {
            return null;
        }
    }

    public void setValue(IntegerRange value) {
        this.value = value;
        value.setInicio(inicio.getValue());
        value.setFin(fin.getValue());
    }
}


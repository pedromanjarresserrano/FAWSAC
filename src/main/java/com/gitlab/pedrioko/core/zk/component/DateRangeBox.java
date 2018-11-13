package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.DateRange;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;

public class DateRangeBox extends Div {
    Datebox fin = new Datebox();
    Datebox inicio = new Datebox();
    private DateRange value;

    public DateRangeBox() {
        value = new DateRange();
        load();
    }

    private void load() {
        inicio.setWidth("100%");
        fin.setWidth("100%");
        appendChild(inicio);
        appendChild(fin);
    }

    public DateRange getValue() {
        value.setInicio(inicio.getValue());
        value.setFin(fin.getValue());
        return value;
    }

    public void setValue(DateRange value) {
        this.value = value;
        value.setInicio(inicio.getValue());
        value.setFin(fin.getValue());
    }
}


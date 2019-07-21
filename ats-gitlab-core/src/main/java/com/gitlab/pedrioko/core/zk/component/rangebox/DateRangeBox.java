package com.gitlab.pedrioko.core.zk.component.rangebox;

import com.gitlab.pedrioko.core.lang.DateRange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;

import java.util.Date;

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
        Date inicio = this.inicio.getValue();

        Date fin = this.fin.getValue();
        if (inicio != null && fin != null) {
            value = new DateRange();
            this.value.setInicio(inicio);
            this.value.setFin(fin);
            return this.value;
        } else {
            return null;
        }
    }

    public void setValue(DateRange value) {
        this.value = value;
        if (value != null) {
            value.setInicio(inicio.getValue());
            value.setFin(fin.getValue());
        } else {
            inicio.setValue(null);
            fin.setValue(null);
            value.setInicio(null);
            value.setFin(null);
        }
    }

    @Override
    public boolean addEventListener(String evtnm, EventListener<? extends Event> listener) {
        switch (evtnm) {
            case Events.ON_CHANGE:
            case Events.ON_CHANGING: {
                fin.addEventListener(evtnm, listener);
                break;
            }
        }

        return super.addEventListener(evtnm, listener);
    }
}


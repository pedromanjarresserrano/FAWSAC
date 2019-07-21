package com.gitlab.pedrioko.core.zk.component.rangebox;

import com.gitlab.pedrioko.core.lang.DoubleRange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;

public class DoubleRangeBox extends Div {
    Doublebox fin = new Doublebox();
    Doublebox inicio = new Doublebox();
    private DoubleRange value;

    public DoubleRangeBox() {
        value = new DoubleRange();
        load();
    }

    private void load() {
        inicio.setWidth("100%");
        fin.setWidth("100%");
        appendChild(inicio);
        appendChild(fin);
    }

    public DoubleRange getValue() {
        Double inicio = this.inicio.getValue();

        Double fin = this.fin.getValue();
        if (inicio != null && fin != null) {
            value = new DoubleRange();
            this.value.setInicio(inicio);
            this.value.setFin(fin);
            return this.value;
        } else {
            return null;
        }
    }

    public void setValue(DoubleRange value) {
        this.value = value;
        value.setInicio(inicio.getValue());
        value.setFin(fin.getValue());
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


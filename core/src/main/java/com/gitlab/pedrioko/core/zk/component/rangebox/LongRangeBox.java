package com.gitlab.pedrioko.core.zk.component.rangebox;

import com.gitlab.pedrioko.core.lang.LongRange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Longbox;

public class LongRangeBox extends Div {
    Longbox fin = new Longbox();
    Longbox inicio = new Longbox();
    private LongRange value;

    public LongRangeBox() {
        value = new LongRange();
        this.setSclass("ats-rangebox");
        load();
    }

    private void load() {
        inicio.setClass("ats-rangebox-input");
        fin.setClass("ats-rangebox-input");
        appendChild(inicio);
        appendChild(fin);
    }

    public LongRange getValue() {
        Long inicio = this.inicio.getValue();

        Long fin = this.fin.getValue();
        if (inicio != null && fin != null) {
            this.value.setInicio(inicio);
            this.value.setFin(fin);
            return this.value;
        } else {
            return null;
        }
    }

    public void setValue(LongRange value) {
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


package com.gitlab.pedrioko.core.zk.widgets;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;

public class Clock extends Div {

    public Clock() {
        Div div = new Div();
        div.setClass("clock");
        div.setHflex("1");
        div.setVflex("1");
        Div div1 = new Div();
        div.appendChild(div1);
        div1.setClass("clock-box");
        Label hour = getLabel(div1, "Hours");

        Label min = getLabel(div1, "Minutes");

        Label sec = getLabel(div1, "Seconds");

        Div div5 = new Div();
        div1.appendChild(div5);
        div5.setClass("clock-field");
        Label ampm = new Label("AM");
        ampm.setClass("clock-field-value");
        div5.appendChild(ampm);
        appendChild(div);
        setHeight("110px");
        Timer timer = new Timer();
        timer.addEventListener(Events.ON_TIMER, e -> {
            java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT-5:00"));
            String valueOf = String.valueOf(cal.get(java.util.Calendar.MINUTE));
            min.setValue(valueOf.length() == 1 ? "0" + valueOf : valueOf);
            hour.setValue(String.valueOf(cal.get(java.util.Calendar.HOUR)));
            valueOf = String.valueOf(cal.get(java.util.Calendar.SECOND));
            sec.setValue(valueOf.length() == 1 ? "0" + valueOf : valueOf);
            ampm.setValue(String.valueOf(cal.get(java.util.Calendar.AM_PM) == 1 ? "PM" : "AM"));
        });
        appendChild(timer);
        timer.start();
        timer.setDelay(1000);
        timer.setRepeats(true);
    }

    public Label getLabel(Div div1, String seconds) {
        Div div4 = new Div();
        div1.appendChild(div4);
        div4.setClass("clock-field");
        Label sectitle = new Label(seconds);
        sectitle.setClass("clock-field-label");
        Label sec = new Label("AM");
        sec.setClass("clock-field-value");
        div4.appendChild(sectitle);
        div4.appendChild(sec);
        return sec;
    }
}

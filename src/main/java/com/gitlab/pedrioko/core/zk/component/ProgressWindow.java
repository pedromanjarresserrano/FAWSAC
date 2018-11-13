package com.gitlab.pedrioko.core.zk.component;

import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class ProgressWindow extends Window {

    private final Integer maxvalue;
    private final Label status;
    private Div progressbar;

    public ProgressWindow(String processname, Integer maxvalue) {
        progressbar = new Div();
        progressbar.setZclass("ldBar");
        this.maxvalue = maxvalue;
        this.setStyle("col-md-12 col-xs-12 col-lg-12");
        this.setTitle(processname);
        status = new Label("");
        this.appendChild(status);
        this.setWidth("300px");
        this.setHeight("100px");
        this.appendChild(progressbar);
    }

    public void setCurrent(Integer value) {
        setCurrent("", value);
    }

    public void setCurrent(String status, Integer value) {
//        this.status.setValue(status);
        Clients.evalJavaScript("" +
                "  var bar2 = document.getElementById('" + progressbar.getUuid() + "').ldBar;\n"
                + "var bar1 = new ldBar(\"#" + progressbar.getUuid() + "\");\n  "
                + "bar2.set('" + getPercent(value) + "');"
                + "bar1.set('" + getPercent(value) + "');");
        if (value == maxvalue)
            this.detach();
    }

    private Integer getPercent(Integer value) {
        return value * 100 / maxvalue;

    }
}

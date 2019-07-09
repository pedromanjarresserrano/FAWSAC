package com.gitlab.pedrioko.core.zk.component.colorchooserbox;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;

public class ColorChooserBox extends Textbox {

    private static final long serialVersionUID = 1L;

    private String value;

    public ColorChooserBox() {
        super();
        setZclass("jscolor");
        addEventListener(Events.ON_AFTER_SIZE, (e) -> {
            String id = getUuid();
            Clients.evalJavaScript("new jscolor($('.jscolor').last()[0]);");

        });

    }

}

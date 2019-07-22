package com.gitlab.pedrioko.core.zk.component.colorpicker;

import lombok.Data;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

import java.io.IOException;

public
class ColorPicker extends Textbox {


    public ColorPicker() {
    }

    public ColorPicker(String value) {
        this.setValue(value);
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "value", this.getValue());
    }

    @Override
    public void service(AuRequest request, boolean everError) {
        super.service(request, everError);
    }
}

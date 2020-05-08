package com.gitlab.pedrioko.core.zk.component.colorpicker;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.Textbox;

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

package com.gitlab.pedrioko.core.zk.component.listbox;

import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.Listbox;

import java.io.IOException;

public class ListBox extends Listbox {

    private transient Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        smartUpdate("value", value);
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "value", value);
    }

}

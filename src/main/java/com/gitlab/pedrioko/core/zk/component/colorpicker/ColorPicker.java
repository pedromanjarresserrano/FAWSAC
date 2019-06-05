package com.gitlab.pedrioko.core.zk.component.colorpicker;

import lombok.Data;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;

public @Data
class ColorPicker extends HtmlBasedComponent {
    private String value = "";


    public ColorPicker() {
    }

    public ColorPicker(String value) {
        this.value = value;
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "value", value);
    }

    public void setValue(String value) {
        this.value = value;
        smartUpdate("value", value);
    }

    public void updateValue(Event evt) throws Exception {
        Object data = evt.getData();
        if (data != null) {
            this.value = (String) data;
            for (EventListener<? extends Event> e : this.getEventListeners(Events.ON_CHANGE)) {
                e.onEvent(null);
            }
        }
    }
}

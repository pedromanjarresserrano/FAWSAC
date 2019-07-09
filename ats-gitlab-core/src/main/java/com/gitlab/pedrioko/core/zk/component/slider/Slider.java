package com.gitlab.pedrioko.core.zk.component.slider;

import com.gitlab.pedrioko.core.view.api.Event;
import lombok.Data;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;
import java.util.Map;

public @Data
class Slider extends HtmlBasedComponent {
    private Map<String, Event> value;

    public Slider() {
    }

    public Slider(Map<String, Event> value) {
        this.value = value;
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "value", value);
    }

    public void setValue(Map<String, Event> value) {
        this.value = value;
        smartUpdate("value", value.keySet());
    }


    public Event get(Object key) {
        return value.get(key);
    }

    public Event put(String key, Event value) {
        return this.value.put(key, value);
    }

}

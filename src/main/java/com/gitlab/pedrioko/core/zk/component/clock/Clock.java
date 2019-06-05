package com.gitlab.pedrioko.core.zk.component.clock;

import lombok.Data;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;

public @Data
class Clock extends HtmlBasedComponent {
    private String location = "";


    public Clock() {
    }

    public Clock(String location) {
        this.location = location;
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "location", location);
    }

    public void setLocation(String location) {
        this.location = location;
        smartUpdate("location", location);
    }

}

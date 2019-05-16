package com.gitlab.pedrioko.core.zk.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.pedrioko.core.view.api.ChosenItem;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.StorageService;
import lombok.Data;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleCarousel extends HtmlBasedComponent {

    private List<String> value = new LinkedList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public SimpleCarousel() {
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "value", objectMapper.writeValueAsString(value));
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        try {
            this.value = value;
            smartUpdate("value", objectMapper.writeValueAsString(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

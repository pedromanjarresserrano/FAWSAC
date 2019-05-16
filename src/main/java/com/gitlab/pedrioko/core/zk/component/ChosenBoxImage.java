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

public @Data
class ChosenBoxImage extends HtmlBasedComponent {
    private Map<Long, ChosenItem> model = new LinkedHashMap<>();
    private List<Object> value = new LinkedList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public ChosenBoxImage() {
    }

    public ChosenBoxImage(Map<Long, ChosenItem> model) {
        this.model = model;
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        List<HashMap> list = new ArrayList<>();
        model.forEach((k, v) -> {
            HashMap<Object, Object> hashMap = new HashMap<>();
            hashMap.put("key", k);
            hashMap.put("image", ApplicationContextUtils.getBean(StorageService.class).getUrlFile(v.filesEntities().get(0)));
            hashMap.put("label", v.visualName());
            list.add(hashMap);
        });
        render(renderer, "model", objectMapper.writeValueAsString(list));
    }

    public void setModel(Map<Long, ChosenItem> model) {
        try {
            this.model = model;
            List<HashMap> list = new ArrayList<>();
            model.forEach((k, v) -> {
                HashMap<Object, Object> hashMap = new HashMap<>();
                hashMap.put("key", k);
                hashMap.put("image", ApplicationContextUtils.getBean(StorageService.class).getUrlFile(v.filesEntities().get(0)));
                hashMap.put("label", v.visualName());
                list.add(hashMap);
            });
            smartUpdate("model", objectMapper.writeValueAsString(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateValue(Event evt) throws Exception {
        JSONObject data = (JSONObject) evt.getData();
        if (data != null) {
            this.value.clear();
            JSONArray value = (JSONArray) data.get("value");
            this.value = value.stream().map(o -> model.get(Long.parseLong(o.toString()))).collect(Collectors.toList());
            for (EventListener<? extends Event> e : this.getEventListeners(Events.ON_CHANGE)) {
                e.onEvent(null);
            }
        }
    }

    public List<Object> getValue() {
        return new ArrayList<>(value);
    }

    public void setValue(List value) {
        try {
            this.value = value;
            List<Long> values = new ArrayList<>();
            model.forEach((k, v) -> {
                if (value.contains(v)) {
                    values.add(k);
                }
            });
            smartUpdate("value", objectMapper.writeValueAsString(values));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

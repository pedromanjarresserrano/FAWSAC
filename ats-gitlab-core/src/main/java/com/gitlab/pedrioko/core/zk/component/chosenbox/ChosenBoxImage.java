package com.gitlab.pedrioko.core.zk.component.chosenbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.pedrioko.core.view.api.ChosenItem;
import com.gitlab.pedrioko.core.reflection.ReflectionJavaUtil;
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
    private Map<Object, ChosenItem> model = new LinkedHashMap<>();
    private List<Object> value = new LinkedList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public ChosenBoxImage() {
    }

    public ChosenBoxImage(Map<Object, ChosenItem> model) {
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

    public void setModel(Map<Object, ChosenItem> model) {
        try {
            this.model = model;
            List<HashMap> list = new ArrayList<>();
            model.forEach((k, v) -> {
                HashMap<String, Object> hashMap = new HashMap<>();
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

    public void setModel(List<?> listmodel) {
        try {
            this.model = new LinkedHashMap<>();
            List<HashMap> list = new LinkedList<>();
            listmodel.stream().forEachOrdered(e -> {
                model.put(ReflectionJavaUtil.getIdValue(e), (ChosenItem) e);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("key", ReflectionJavaUtil.getIdValue(e));
                hashMap.put("image", ApplicationContextUtils.getBean(StorageService.class).getUrlFile(((ChosenItem) e).filesEntities().get(0)));
                hashMap.put("label", ((ChosenItem) e).visualName());
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
            if (value != null) this.value.clear();
            JSONArray value = (JSONArray) data.get("value");
            if (value != null && !"null".equalsIgnoreCase((String) value.get(0))) {
                this.value = value.stream().map(o -> model.get(Long.parseLong(o.toString()))).collect(Collectors.toList());
            } else {
                List<Object> objects = new ArrayList<>();
                objects.add(null);
                this.value = objects;
            }
            for (EventListener<? extends Event> e : this.getEventListeners(Events.ON_CHANGE)) {
                e.onEvent(null);
            }
        }
    }

    public List<Object> getValue() {
        if (value != null)
            return new ArrayList<>(value);
        else
            return Collections.emptyList();
    }

    public void setValue(List value) {
        try {
            this.value = value;
            List<Object> values = new ArrayList<>();
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

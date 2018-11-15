package com.gitlab.pedrioko.core.view.viewers;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.api.OnEvent;
import com.gitlab.pedrioko.core.view.enums.CrudEvents;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.Validate;
import com.gitlab.pedrioko.core.zk.component.Carousel;
import com.gitlab.pedrioko.core.zk.component.model.CarouselItem;
import com.gitlab.pedrioko.services.StorageService;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrudGrid extends Grid {

    private static final long serialVersionUID = 1L;
    private final transient List listitems = new ArrayList();
    private final Map<CrudEvents, List<OnEvent>> onEvent = new HashMap<>();
    private Columns columns;
    private Rows rows;
    private Class<?> klass;
    private Object selectValue;
    private String selectValueUUID = "";
    private CrudMenuContext popup;
    private StorageService storageService;
    private int cellsInRow = 4;


    public CrudGrid(Class<?> klass) {
        super();
        this.klass = klass;
        init(klass);
    }

    public CrudGrid(Class<?> klass, List<Class<?>> all) {
        super();
        listitems.clear();
        listitems.addAll(all);
        init(klass);
    }

    private void init(Class<?> klass) {
        columns = new Columns();
        columns.setParent(this);
        rows = new Rows();
        rows.setParent(this);
        columns.appendChild(new Column("Prueba"));
        this.klass = klass;
        if (ReflectionZKUtil.isInEventListener()) {
            setAutopaging(true);
            setMold("paging");
            setPageSize(24);
        }
        setVflex("1");
        setHflex("1");
        setStyle("height:98;");
        rows.setHeight("135px");
        popup = new CrudMenuContext(klass, ApplicationContextUtils.getBeans(Action.class));
        storageService = ApplicationContextUtils.getBean(StorageService.class);
    }

    private void loadItems() {
        if (listitems != null) {
            this.getRows().getChildren().clear();
            int counter = 0;
            Row row = new Row();
            Map<String, Object> map = Validate.getGridViewFieldPreview(klass);
            Object value = map.get("value");
            String gridViewFieldName = Validate.getGridViewFieldName(klass);
            for (int i = 0; i < listitems.size(); i++) {
                Vlayout child = new Vlayout();
                Object obj = listitems.get(i);

                Object valueFieldObject = ReflectionJavaUtil.getValueFieldObject((String) value, obj);

                if ((boolean) map.get("isList")) {
                    List<FileEntity> listfiles = (List) valueFieldObject;
                    if (!listfiles.isEmpty()) {
                        Carousel carousel = new Carousel();
                        carousel.setCarouselItems(listfiles.stream().map(e -> {
                            CarouselItem carouselItem = new CarouselItem();
                            String url = ApplicationContextUtils.getBean(StorageService.class).getUrlFile(e.getFilename());
                            carouselItem.setEnlargedSrc(url);
                            return carouselItem;
                        }).collect(Collectors.toList()));
                        child.appendChild(carousel);

                    }
                } else {
                    Image image = new Image();
                    image.setSrc(storageService.getUrlFile(((FileEntity) valueFieldObject)));
                    image.setClass("img-responsive");
                    image.setStyle("margin: 0 auto; background: black;");
                    image.setHeight("100px");
                    child.appendChild(image);
                }


                child.setClass("crud-grid-item");
                child.appendChild(new Label((String) ReflectionJavaUtil.getValueFieldObject(gridViewFieldName, obj)));
                child.setHeight("135px");
                int finalI = i;
                Div div = new Div();
                div.appendChild(child);
                child.addEventListener(Events.ON_CLICK, (e) -> {
                    onClick(child, finalI);
                    popup.open(child, "after_end");
                    getEvent(CrudEvents.ON_CLICK).forEach(OnEvent::doSomething);
                });
                child.addEventListener(Events.ON_RIGHT_CLICK, (e) -> {
                    onClick(child, finalI);
                    popup.open(child, "after_end");
                    getEvent(CrudEvents.ON_RIGHT_CLICK).forEach(OnEvent::doSomething);
                });
                row.appendChild(div);
                row.setHeight("135px");
                counter++;

                if (counter == this.cellsInRow || listitems.size() < this.cellsInRow + 1) {
                    rows.appendChild(row);
                    row = new Row();
                    counter = 0;
                }
            }
        }
        System.out.println("end load");

    }

    private void onClick(Vlayout child, int finalI) {
        selectValue = listitems.get(finalI);
        if (!selectValueUUID.isEmpty())
            Clients.evalJavaScript("var var1 = document.getElementById('" + selectValueUUID + "');" +
                    "if(var1 != null) var1.style.opacity = '1.0';");

        Clients.evalJavaScript("document.getElementById('" + child.getUuid() + "').style.opacity = '0.5'");

        selectValueUUID = child.getUuid();
    }

    private List<OnEvent> getEvent(CrudEvents events) {
        List<OnEvent> onEvents = this.onEvent.get(CrudEvents.ON_RIGHT_CLICK);
        return onEvents == null ? new ArrayList<>() : onEvents;
    }

    public <T> T getSelectedValue() {
        return (T) selectValue;
    }

    public List getValue() {
        return listitems;
    }

    public void setValue(List<?> all) {
        listitems.clear();
        listitems.addAll(all);
        loadItems();
    }


    public void addEventOnEvent(CrudEvents e, OnEvent o) {
        List<OnEvent> onEvents = this.onEvent.get(e);
        if (onEvents == null) {
            onEvents = new ArrayList<>();
        }
        onEvents.add(o);
        this.onEvent.put(e, onEvents);
    }

    public void update() {
        loadItems();

    }


}

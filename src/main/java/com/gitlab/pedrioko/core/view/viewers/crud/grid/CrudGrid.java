package com.gitlab.pedrioko.core.view.viewers.crud.grid;

import com.gitlab.pedrioko.core.view.api.CrudDisplayTable;
import com.gitlab.pedrioko.core.view.api.CrudGridItem;
import com.gitlab.pedrioko.core.view.api.OnEvent;
import com.gitlab.pedrioko.core.view.enums.CrudEvents;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.services.StorageService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.collections4.map.LinkedMap;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
public class CrudGrid extends Borderlayout implements CrudDisplayTable {

    private static final long serialVersionUID = 1L;
    private final transient List listitems = new ArrayList();
    private final Map<CrudEvents, List<OnEvent>> onEvent = new HashMap<>();
    private Columns columns;
    private Rows rows;
    private Class<?> klass;
    private Object selectValue;
    private String selectValueUUID = "";
    private StorageService storageService;
    private int cellsInRow = 4;
    private Grid grid = new Grid();
    private @Getter
    Long imageHeight;
    //private int PAGE_SIZE = 16;
    //private Paging paging;
    private Map<Float, GridItem> gridItemMap = new LinkedMap<>();

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
        if (CrudGridItem.class.isAssignableFrom(klass)) {
            grid.setOddRowSclass("color-system");
            columns = new Columns();
            columns.setParent(grid);
            rows = new Rows();
            rows.setParent(grid);
            rows.setSclass("color-system");
            columns.setSclass("color-system");
            columns.appendChild(new Column(""));
            grid.appendChild(columns);
            grid.appendChild(rows);
            grid.setSclass("color-system");
            this.klass = klass;
            setVflex("1");
            setHflex("1");
            setStyle("height:100%; width:100%; overflow-y:auto !important;");
            grid.setStyle("width:100%;");
            rows.setHeight("135px");
            storageService = ApplicationContextUtils.getBean(StorageService.class);
            imageHeight = 100L;
          /*  paging = new Paging();
            paging.setDetailed(true);
            if (ZKUtil.isMobile())
                paging.setMold("os");
            paging.setPageSize(PAGE_SIZE);
            paging.addEventListener("onPaging", (Event event) -> {
                PagingEvent pe = (PagingEvent) event;
                int pgno = pe.getActivePage();
                int ofs = pgno * PAGE_SIZE;
                redraw(ofs, PAGE_SIZE);
            });*/
            Center center = new Center();
            center.appendChild(grid);
            center.setStyle("overflow-y:auto !important;");
            appendChild(center);
            South south = new South();
            //     south.appendChild(paging);
            appendChild(south);

        } else {
            throw new IllegalArgumentException("Class " + klass + " not implement interface CrudGridItem");
        }

    }

    private void redraw(List<?> page) {
        if (listitems != null) {
            cellsInRow = ZKUtil.isMobile() ? 1 : cellsInRow;

            grid.getRows().getChildren().clear();
            int counter = 0;
            Row row = new Row();
            row.setSclass("color-system");
            int size = page.size();
            for (Object i : page) {
                CrudGridItem obj = (CrudGridItem) i;
                GridItem child = new GridItem(obj, imageHeight);
                gridItemMap.put(Float.parseFloat(ReflectionJavaUtil.getIdValue(obj).toString()), child);

                child.addEventListener(Events.ON_CLICK, (e) -> {
                    onClick(child, obj);
                    if (ZKUtil.isMobile())
                        getEvent(CrudEvents.ON_RIGHT_CLICK).forEach(OnEvent::doSomething);
                });
                child.addEventListener(Events.ON_RIGHT_CLICK, (e) -> {
                    onClick(child, obj);
                    getEvent(CrudEvents.ON_RIGHT_CLICK).forEach(OnEvent::doSomething);
                });
                Div div = new Div();
                div.appendChild(child);
                rows.addEventListener(Events.ON_RIGHT_CLICK, (e) -> {
                    onClick(child, obj);
                    getEvent(CrudEvents.ON_RIGHT_CLICK).forEach(OnEvent::doSomething);
                });

                row.appendChild(div);
                row.setHeight("auto");
                counter++;
                if (page.indexOf(i) + 1 == page.size() && counter < cellsInRow) {
                    int left = counter - cellsInRow;
                    if (left < 0)
                        left = left * -1;
                    for (int w = 0; w < left; w++) {
                        row.appendChild(new Div());
                        counter++;
                    }
                }
                if (counter == cellsInRow || (size < cellsInRow && counter == size) || page.indexOf(i) + 1 == page.size()) {
                    rows.appendChild(row);
                    row = new Row();
                    counter = 0;
                }
            }
        }
    }


    private void onClick(Vlayout child, Object obj) {
        selectValue = obj;
        if (!selectValueUUID.isEmpty())
            Clients.evalJavaScript("var var1 = document.getElementById('" + selectValueUUID + "');" +
                    "if(var1 != null) {" +
                    "var1.style.border= '0px';" +
                    "}");


        Clients.evalJavaScript("document.getElementById('" + child.getUuid() + "').style.border=  '1px solid';" +
                "");

        selectValueUUID = child.getUuid();
    }

    private List<OnEvent> getEvent(CrudEvents events) {
        List<OnEvent> onEvents = onEvent.get(CrudEvents.ON_RIGHT_CLICK);
        return onEvents == null ? new ArrayList<>() : onEvents;
    }

    @Override
    public <T> T getSelectedValue() {
        return (T) selectValue;
    }

    @Override
    public void clearSelection() {
        selectValue = null;
    }

    @Override
    public List getValue() {
        return listitems;
    }

    @Override
    public void setValue(List<?> all) {
        listitems.clear();
        listitems.addAll(all);
      /*  paging.setTotalSize(all.size());
        paging.setPageSize(PAGE_SIZE);*/
        update();
    }

    @Override
    public void updateValue(Object value) {
        if (CrudGridItem.class.isAssignableFrom(value.getClass())) {
            float key = Float.parseFloat(ReflectionJavaUtil.getIdValue(value).toString());
            GridItem gridItem = gridItemMap.get(key);
            listitems.set(listitems.indexOf(gridItem.getValue()), value);
            gridItem.update((CrudGridItem) value);
        }
    }


    public void addEventOnEvent(CrudEvents e, OnEvent o) {
        List<OnEvent> onEvents = onEvent.get(e);
        if (onEvents == null) {
            onEvents = new ArrayList<>();
        }
        onEvents.add(o);
        onEvent.put(e, onEvents);
    }

    @Override
    public void update() {
        //  paging.setTotalSize(listitems.size());
        //   paging.setPageSize(PAGE_SIZE);
        redraw(listitems);
    }

    public void setPageSize(int pagesize) {
        //    this.PAGE_SIZE = pagesize;
        update();
    }

    public void setImageHeight(long imageHeight) {
        this.imageHeight = imageHeight;
        update();
    }

}

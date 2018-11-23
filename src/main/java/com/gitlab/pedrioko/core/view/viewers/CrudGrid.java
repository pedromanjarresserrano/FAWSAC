package com.gitlab.pedrioko.core.view.viewers;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.api.CrudDisplayTable;
import com.gitlab.pedrioko.core.view.api.CrudGridItem;
import com.gitlab.pedrioko.core.view.api.OnEvent;
import com.gitlab.pedrioko.core.view.enums.CrudEvents;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.zk.component.Carousel;
import com.gitlab.pedrioko.core.zk.component.Video;
import com.gitlab.pedrioko.core.zk.component.model.CarouselItem;
import com.gitlab.pedrioko.services.StorageService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.poi.ss.formula.functions.T;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;
import org.zkoss.zul.event.PagingEvent;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
public class CrudGrid extends Div implements CrudDisplayTable {

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
    private Grid content = new Grid();
    private @Getter
    Long imageHeight;
    private int PAGE_SIZE = 16;
    private Paging paging;

    CrudGrid(Class<?> klass) {
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
            columns = new Columns();
            columns.setParent(content);
            rows = new Rows();
            rows.setParent(content);
            columns.appendChild(new Column(" "));
            this.klass = klass;
            if (ReflectionZKUtil.isInEventListener()) {
                content.setAutopaging(false);
                setMold("paging");
            }
            content.setVflex("1");
            content.setHflex("1");
            content.setStyle("height:98;");
            this.setVflex("1");
            this.setHflex("1");
            this.setStyle("height:98;");
            rows.setHeight("135px");
            storageService = ApplicationContextUtils.getBean(StorageService.class);
            imageHeight = 100L;
            paging = new Paging();
            paging.setPageSize(PAGE_SIZE);
            paging.addEventListener("onPaging", (Event event) -> {
                PagingEvent pe = (PagingEvent) event;
                int pgno = pe.getActivePage();
                int ofs = pgno * PAGE_SIZE;
                // Redraw current paging
                redraw(ofs, PAGE_SIZE);
            });
            this.appendChild(content);
            this.appendChild(paging);
        } else {
            throw new IllegalArgumentException("Class " + klass + " not implement interface CrudGridItem");
        }
    }

    private void redraw(int firstResult, int maxResults) {
        if (listitems != null) {
            int fm = firstResult + maxResults;
            List page = listitems.subList(firstResult, listitems.size() < fm ?
                    listitems.size() : fm);
            content.getRows().getChildren().clear();
            int counter = 0;
            Row row = new Row();
            int size = page.size();
            for (int i = 0; i < size; i++) {
                Vlayout child = new Vlayout();
                CrudGridItem obj = (CrudGridItem) page.get(i);
                List<FileEntity> listfiles = obj.getFilesEntities();
                if (obj.isCarrouselPreview()) {
                    if (!listfiles.isEmpty()) {
                        Carousel carousel = new Carousel();
                        carousel.setLazyload(true);
                        carousel.setCarouselItems(listfiles.stream().sorted(Comparator.comparingLong(x -> x.getId())).map(e -> {
                            CarouselItem carouselItem = new CarouselItem();
                            String url = ApplicationContextUtils.getBean(StorageService.class).getUrlFile(e.getFilename());
                            carouselItem.setEnlargedSrc(url);
                            carouselItem.setEnlargedHeight(imageHeight + "px");

                            return carouselItem;
                        }).collect(Collectors.toList()));
                        child.appendChild(carousel);

                    } else {
                        String urlFile = "/statics/files/" + obj.getName();
                        if (urlFile.endsWith(".gif")) {
                            Image image = new Image();
                            image.setClass("img-responsive");
                            image.setStyle("margin: auto;");
                            image.setSrc(urlFile);
                            image.setHeight(imageHeight.toString() + "px");
                            child.appendChild(image);
                        }
                        if (urlFile.endsWith(".webm")) {

                            Video image = new Video();
                            image.setSrc(urlFile);
                            image.setHeight(imageHeight.toString() + "px");
                            child.appendChild(image);
                        }
                    }
                } else {
                    if (!listfiles.isEmpty()) {
                        String urlFile = storageService.getUrlFile(listfiles.get(0));
                        Image image = new Image();
                        image.setClass("img-responsive");
                        image.setSrc(urlFile);
                        image.setStyle("margin: auto;");
                        image.setHeight(imageHeight.toString() + "px");
                        child.appendChild(image);
                    }
                }

                child.setClass("crud-grid-item");
                String visualName = obj.getVisualName();
                child.appendChild(new Label(visualName == null || visualName.isEmpty() ? obj.getName() : visualName));
                child.setHeight("auto");
                int finalI = i;
                child.addEventListener(Events.ON_CLICK, (e) -> {
                    onClick(child, finalI);
                });
                child.addEventListener(Events.ON_RIGHT_CLICK, (e) -> {
                    onClick(child, finalI);
                    getEvent(CrudEvents.ON_RIGHT_CLICK).forEach(OnEvent::doSomething);
                });
                Div div = new Div();
                div.appendChild(child);
                rows.addEventListener(Events.ON_RIGHT_CLICK, (e) -> {
                    onClick(child, finalI);
                    getEvent(CrudEvents.ON_RIGHT_CLICK).forEach(OnEvent::doSomething);
                });

                row.appendChild(div);
                row.setHeight("auto");
                counter++;

                if (counter == this.cellsInRow || (size < this.cellsInRow && counter == size) || (i == size - 1)) {
                    rows.appendChild(row);
                    row = new Row();
                    counter = 0;
                }
            }
        }
    }

    private void loadItems() {
        if (listitems != null) {
            content.getRows().getChildren().clear();
            int counter = 0;
            Row row = new Row();
            int size = listitems.size();
            for (int i = 0; i < size; i++) {
                Vlayout child = new Vlayout();
                CrudGridItem obj = (CrudGridItem) listitems.get(i);
                List<FileEntity> listfiles = obj.getFilesEntities();
                if (obj.isCarrouselPreview()) {
                    if (!listfiles.isEmpty()) {
                        Carousel carousel = new Carousel();
                        carousel.setLazyload(true);
                        carousel.setCarouselItems(listfiles.stream().sorted(Comparator.comparingLong(x -> x.getId())).map(e -> {
                            CarouselItem carouselItem = new CarouselItem();
                            String url = ApplicationContextUtils.getBean(StorageService.class).getUrlFile(e.getFilename());
                            carouselItem.setEnlargedSrc(url);
                            carouselItem.setEnlargedHeight(imageHeight + "px");

                            return carouselItem;
                        }).collect(Collectors.toList()));
                        child.appendChild(carousel);

                    } else {
                        String urlFile = "/statics/files/" + obj.getName();
                        if (urlFile.endsWith(".gif")) {
                            Image image = new Image();
                            image.setClass("img-responsive");
                            image.setStyle("margin: auto;");
                            image.setSrc(urlFile);
                            image.setHeight(imageHeight.toString() + "px");
                            child.appendChild(image);
                        }
                        if (urlFile.endsWith(".webm")) {

                            Video image = new Video();
                            image.setSrc(urlFile);
                            image.setHeight(imageHeight.toString() + "px");
                            child.appendChild(image);
                        }
                    }
                } else {
                    if (!listfiles.isEmpty()) {
                        String urlFile = storageService.getUrlFile(listfiles.get(0));
                        Image image = new Image();
                        image.setClass("img-responsive");
                        image.setSrc(urlFile);
                        image.setStyle("margin: auto;");
                        image.setHeight(imageHeight.toString() + "px");
                        child.appendChild(image);
                    }
                }

                child.setClass("crud-grid-item");
                String visualName = obj.getVisualName();
                child.appendChild(new Label(visualName == null || visualName.isEmpty() ? obj.getName() : visualName));
                child.setHeight("auto");
                int finalI = i;
                child.addEventListener(Events.ON_CLICK, (e) -> {
                    onClick(child, finalI);
                });
                child.addEventListener(Events.ON_RIGHT_CLICK, (e) -> {
                    onClick(child, finalI);
                    getEvent(CrudEvents.ON_RIGHT_CLICK).forEach(OnEvent::doSomething);
                });
                Div div = new Div();
                div.appendChild(child);
                rows.addEventListener(Events.ON_RIGHT_CLICK, (e) -> {
                    onClick(child, finalI);
                    getEvent(CrudEvents.ON_RIGHT_CLICK).forEach(OnEvent::doSomething);
                });

                row.appendChild(div);
                row.setHeight("auto");
                counter++;

                if (counter == this.cellsInRow || (size < this.cellsInRow && counter == size) || (i == size - 1)) {
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
                    "if(var1 != null) {" +
                    "var1.style.border= '0px';" +
                    "}");


        Clients.evalJavaScript("document.getElementById('" + child.getUuid() + "').style.border=  '2px solid';" +
                "");

        selectValueUUID = child.getUuid();
    }

    private List<OnEvent> getEvent(CrudEvents events) {
        List<OnEvent> onEvents = this.onEvent.get(CrudEvents.ON_RIGHT_CLICK);
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
        paging.setTotalSize(all.size());
        loadItems();
    }


    void addEventOnEvent(CrudEvents e, OnEvent o) {
        List<OnEvent> onEvents = this.onEvent.get(e);
        if (onEvents == null) {
            onEvents = new ArrayList<>();
        }
        onEvents.add(o);
        this.onEvent.put(e, onEvents);
    }

    @Override
    public void update() {
        redraw(0, PAGE_SIZE);
    }


    public void setImageHeight(long imageHeight) {
        this.imageHeight = imageHeight;
        update();
    }

    public void setMold(String mold) {
        content.setMold(mold);
    }
}

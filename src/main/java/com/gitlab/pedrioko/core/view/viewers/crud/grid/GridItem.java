package com.gitlab.pedrioko.core.view.viewers.crud.grid;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.api.CrudGridItem;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.zk.component.Carousel;
import com.gitlab.pedrioko.core.zk.component.Video;
import com.gitlab.pedrioko.core.zk.component.model.CarouselItem;
import com.gitlab.pedrioko.services.StorageService;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GridItem extends Vlayout {

    private Long imageHeight = 100L;
    private @Getter
    @Setter
    CrudGridItem value;
    private StorageService storageService;

    public GridItem(CrudGridItem value) {
        setClass("crud-grid-item");
        setHeight("auto");
        this.value = value;
        load();
    }

    GridItem(CrudGridItem obj, Long imageHeight) {
        this.imageHeight = imageHeight;
        setClass("crud-grid-item");
        setHeight("auto");
        value = obj;
        load();
    }

    public void update(CrudGridItem crudGridItem, Long imageHeight) {
        this.imageHeight = imageHeight;
        value = crudGridItem;
        load();
    }

    public void update(CrudGridItem crudGridItem) {
        value = crudGridItem;
        load();
    }

    protected void load() {
        getChildren().clear();
        List<FileEntity> listfiles = value.getFilesEntities();
        storageService = ApplicationContextUtils.getBean(StorageService.class);
        if (value.isCarrouselPreview()) {
            if (!listfiles.isEmpty()) {
                Carousel carousel = new Carousel();
                carousel.setLazyload(true);
                carousel.setControls(false);
                carousel.setSlideBy("1");
                carousel.setCarouselItems(listfiles.stream().sorted(Comparator.comparingLong(x -> x.getId())).map(e -> {
                    CarouselItem carouselItem = new CarouselItem();
                    String url = storageService.getUrlFile(e);
                    carouselItem.setEnlargedSrc(url);
                    carouselItem.setEnlargedHeight(imageHeight + "px");
                    return carouselItem;
                }).collect(Collectors.toList()));
                appendChild(carousel);
            } else {
                String urlFile = (storageService.getUrlFile(value.getURL(), true)).replace("//", "/");
                if (urlFile.endsWith(".gif")) {
                    Image image = new Image();
                    image.setClass("img-responsive");
                    image.setStyle("margin: auto;");
                    image.setSrc(urlFile);
                    image.setHeight(imageHeight.toString() + "px");
                    appendChild(image);
                }
                if (urlFile.endsWith(".webm")) {
                    Video image = new Video();
                    image.setSrc(urlFile);
                    image.setHeight(imageHeight.toString() + "px");
                    appendChild(image);
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
                appendChild(image);
            }
        }
        String visualName = value.getVisualName();
        appendChild(new Label(visualName == null || visualName.isEmpty() ? value.getName() : visualName));
    }
}

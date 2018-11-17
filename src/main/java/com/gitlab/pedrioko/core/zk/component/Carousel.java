package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.zk.component.model.CarouselItem;
import com.google.gson.Gson;
import lombok.Data;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public @Data
class Carousel extends HtmlBasedComponent {

    private List<CarouselItem> carouselItems;
    private String carouselItemsJson = "";

    public Carousel() {
    }

    @Override
    public boolean addEventListener(String evtnm, EventListener<? extends Event> listener) {
        return super.addEventListener(evtnm, listener);
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "carouselItems", carouselItems);
        render(renderer, "carouselItemsJson", getCarouselItemsJson());
    }


    public String getCarouselItemsJson() {
        String s = new Gson().toJson(carouselItems != null ? carouselItems : new ArrayList());
        return s;
    }

    public void setCarouselItemsJson(String galleryItemsJson) {
        this.carouselItemsJson = galleryItemsJson;
        smartUpdate("carouselItemsJson", carouselItemsJson);
    }

    public void setCarouselItems(List<CarouselItem> carouselItems) {
        this.carouselItems = carouselItems;
        smartUpdate("carouselItemsJson", carouselItems);

    }
}

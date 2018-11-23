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

    private String mode = "carousel";

    private String axis = "horizontal";

    private int gutter = 0;

    private int edgePadding = 0;

    private String fixedWidth = "false";

    private Boolean autoWidth = false;

    private String viewportMax = "false";

    private String slideBy = "1";

    private Boolean controls = true;

    private String controlsPosition = "top";

    private String[] controlsText = new String[]{"prev", "next"};

    private String controlsContainer = "false";

    private String prevButton = "false";

    private String nextButton = "false";

    private Boolean nav = true;

    private String navPosition = "top";

    private String navContainer = "false";

    private Boolean navAsThumbnails = false;

    private Boolean arrowKeys = false;

    private int speed = 300;

    private Boolean autoplay = false;

    private String autoplayPosition = "top";

    private int autoplayTimeout = 5000;

    private String autoplayDirection = "forward";

    private String[] autoplayText = {"start", "stop"};

    private Boolean autoplayHoverPause = false;

    private String autoplayButton = "false";

    private Boolean autoplayButtonOutput = true;

    private Boolean autoplayResetOnVisibility = true;

    private String animateIn = "tns-fadeIn";

    private String animateOut = "tns-fadeOut";

    private String animateNormal = "tns-normal";

    private String animateDelay = "false";

    private Boolean loop = true;

    private Boolean rewind = false;

    private Boolean autoHeight = false;
    /*
        Object:

        {   responsive = false.
          breakpoint:
            {		  key:value
               }
           } |false*/
    private Boolean lazyload = false;

    private String lazyloadSelector = ".tns-lazy-img";

    private Boolean touch = true;

    private Boolean mouseDrag = false;

    private String swipeAngle = "15";

    private Boolean preventActionWhenRunning = false;

    private String preventScrollOnTouch = "auto";

    private String nested = "false";

    private Boolean freezable = true;

    private Boolean disable = false;

    private int startIndex = 0;

    private String onInit = "false";

    private Boolean useLocalStorage = true;


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
        render(renderer, "lazyload", getLazyload());
        render(renderer, "slideBy", getSlideBy());
        render(renderer, "controls", getControls());
    }


    private String getCarouselItemsJson() {
        String s = new Gson().toJson(carouselItems != null ? carouselItems : new ArrayList());
        return s;
    }

    public void setControls(Boolean controls) {
        this.controls = controls;
        smartUpdate("controls", controls);
    }

    public void setSlideBy(String slideBy) {
        this.slideBy = slideBy;
        smartUpdate("slideBy", slideBy);

    }

    public void setCarouselItemsJson(String galleryItemsJson) {
        this.carouselItemsJson = galleryItemsJson;
        smartUpdate("carouselItemsJson", carouselItemsJson);
    }

    public void setCarouselItems(List<CarouselItem> carouselItems) {
        this.carouselItems = carouselItems;
        smartUpdate("carouselItemsJson", carouselItems);

    }

    public Boolean getLazyload() {
        return lazyload;
    }

    public void setLazyload(Boolean lazyload) {
        this.lazyload = lazyload;
        smartUpdate("lazyload", lazyload);
    }
}

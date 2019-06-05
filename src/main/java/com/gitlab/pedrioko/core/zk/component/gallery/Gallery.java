package com.gitlab.pedrioko.core.zk.component.gallery;

import com.gitlab.pedrioko.core.zk.component.gallery.model.GalleryItem;
import com.gitlab.pedrioko.core.zk.component.gallery.model.enums.GalleryType;
import com.google.gson.Gson;
import lombok.Data;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;
import java.util.List;

public @Data
class Gallery extends HtmlBasedComponent {

    private GalleryType galleryType;

    private List<GalleryItem> galleryItems;
    private JSONObject jsonObject = new JSONObject();
    private String galleryItemsJson = "";

    public Gallery() {
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "galleryType", galleryType);
        render(renderer, "galleryItems", galleryItems);
        render(renderer, "galleryItemsJson", getGalleryItemsJson());
    }


    public String getGalleryItemsJson() {
        String s = new Gson().toJson(galleryItems);
        return s;
    }

    public void setGalleryItemsJson(String galleryItemsJson) {
        this.galleryItemsJson = galleryItemsJson;
        this.smartUpdate("galleryItemsJson", this.galleryItemsJson);
    }


}

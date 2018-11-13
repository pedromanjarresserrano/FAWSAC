package com.gitlab.pedrioko.core.zk.component.model;

import lombok.Data;

public @Data
class GalleryItem {

    private String thumbnailSrc;
    private String thumbnailWidth;
    private String thumbnailHeight;
    private String enlargedSrc;
    private String enlargedWidth;
    private String enlargedHeight;
    private String title;
    private String link;
    private String linkTarget;
    private String color;

    public GalleryItem() {
    }
}

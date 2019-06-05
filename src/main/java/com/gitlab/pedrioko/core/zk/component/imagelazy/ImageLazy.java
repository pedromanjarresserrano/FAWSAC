package com.gitlab.pedrioko.core.zk.component.imagelazy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ImageLazy extends HtmlBasedComponent {

    private String datasrc = "";
    private String src = "";


    public ImageLazy() {
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "_datasrc", datasrc);
        render(renderer, "_src", src);
    }

    public String getDatasrc() {
        return datasrc;
    }

    public void setDatasrc(String datasrc) {
        this.datasrc = datasrc;
        smartUpdate("_datasrc", this.datasrc);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
        smartUpdate("_src", this.src);
    }


}

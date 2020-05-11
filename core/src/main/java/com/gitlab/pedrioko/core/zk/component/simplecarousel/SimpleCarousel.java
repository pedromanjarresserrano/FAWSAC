package com.gitlab.pedrioko.core.zk.component.simplecarousel;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SimpleCarousel extends HtmlBasedComponent {


    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCarousel.class);

    private List<String> value = new LinkedList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public SimpleCarousel() {
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "value", objectMapper.writeValueAsString(value));
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        try {
            this.value = value;
            smartUpdate("value", objectMapper.writeValueAsString(value));
        } catch (Exception e) {
            LOGGER.error("ERROR on setValue()", e);
        }
    }
}

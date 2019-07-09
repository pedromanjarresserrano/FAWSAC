package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.Point;
import com.gitlab.pedrioko.core.lang.annotation.Customizer;
import com.gitlab.pedrioko.core.view.api.RowCustomizer;
import org.zkoss.zul.Div;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Customizer
public class PointRowCustomizer implements RowCustomizer {

    @Override
    public List<?> getToClass() {
        return Arrays.asList(Point.class);
    }

    @Override
    public void customizer(Div div, Field f) {
        div.setZclass("col-sm-12 col-md-12 col-lg-12");
        ((Div) div.getChildren().get(0)).setZclass("col-md-1");
        ((Div) div.getChildren().get(1)).setZclass("col-md-11");
    }

}

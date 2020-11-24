package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.Point;
import com.gitlab.pedrioko.core.lang.Polygon;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.zk.component.gmap.PolygonGmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Component
public class PolygonField implements FieldComponent {
    @Autowired
    private PropertiesUtil properties;

    @Override
    public Class[] getToClass() {
        return new Class[]{Polygon.class};
    }

    @Override
    public Component getComponent(Field e) {
        PolygonGmap maps = new PolygonGmap();
        maps.setValue(new Polygon());
        List<Point> points = new ArrayList<>();
        maps.setHeight("350px");
        maps.setWidth("350px");
        Menupopup popup = new Menupopup();
        popup.setId("reset");
        Menuitem mi = new Menuitem();
        mi.setLabel("Reset");
        maps.addEventListener(Events.ON_RIGHT_CLICK, x -> maps.getValue().setPoints(points));

        popup.appendChild(mi);
        maps.setContext(popup);
        maps.setContext("reset");
        maps.setPopup(popup);
        maps.setLat(properties.getDouble("gmap.lat", 0));
        maps.setLng(properties.getDouble("gmap.lng", 0));
        return maps;
    }

}

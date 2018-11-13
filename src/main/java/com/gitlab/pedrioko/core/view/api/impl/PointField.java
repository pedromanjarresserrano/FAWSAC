package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.Point;
import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.zk.component.PointGmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.Component;

import java.lang.reflect.Field;

@FieldForm
public class PointField implements FieldComponent {
    @Autowired
    private PropertiesUtil properties;

    @Override
    public Class[] getToClass() {
        return new Class[]{Point.class};
    }

    @Override
    public Component getComponent(Field e, EntityForm f) {
        PointGmap maps = new PointGmap();
        maps.setValue(new Point());
        maps.addEventListener("onMapClick", x -> {
            MapMouseEvent w = (MapMouseEvent) x;
            double lat = w.getLatLng().getLatitude();
            double lng = w.getLatLng().getLongitude();
            Point value = maps.getValue();
            value.setLat(lat);
            value.setLng(lng);
            maps.setValue(value);
        });
        maps.setClass("col-md-10 col-xs-10 col-lg-10 col-sm-10");
        maps.setHeight("350px");
        maps.setLat(properties.getDouble("gmap.lat", 0));
        maps.setLng(properties.getDouble("gmap.lng", 0));

        return maps;
    }

}

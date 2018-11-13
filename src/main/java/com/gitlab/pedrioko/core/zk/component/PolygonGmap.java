package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.Point;
import com.gitlab.pedrioko.core.lang.Polygon;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gpolygon;
import org.zkoss.gmaps.LatLng;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
public class PolygonGmap extends Div {
    private transient @Getter
    Polygon value;
    private @Getter
    @Setter
    boolean disabled;
    private Gpolygon gpolygon;
    private Gmaps maps = new Gmaps();
    private List<Point> points;

    public PolygonGmap() {
        super();
        gpolygon = new Gpolygon();
        maps.appendChild(gpolygon);

        maps.addEventListener("onMapClick", x -> {
            MapMouseEvent w = (MapMouseEvent) x;
            LatLng latLng = w.getLatLng();
            Point point = new Point();
            point.setLat(latLng.getLatitude());
            point.setLng(latLng.getLongitude());
            points.add(point);
            Polygon value = getValue();
            value.setPoints(points);
            setValue(value);
        });
        appendChild(maps);
        Button reset = new Button(ReflectionZKUtil.getLabel("reset"));
        reset.setClass("btn-warning");
        reset.addEventListener(Events.ON_CLICK, e -> {
            gpolygon.setPath(new ArrayList());
            points = new ArrayList<>();
            Polygon value = getValue();
            value.setPoints(points);
            setValue(value);
        });
        reset.setIconSclass("fa fa-sync");
        appendChild(reset);
    }

    @Override
    public String getHeight() {
        return maps.getHeight();
    }

    @Override
    public void setHeight(String height) {
        maps.setHeight(height);
    }

    @Override
    public String getWidth() {
        return maps.getWidth();
    }

    @Override
    public void setWidth(String width) {
        maps.setWidth(width);
    }

    /**
     * @param value the value to set
     */
    public void setValue(Polygon value) {
        this.value = value;
        gpolygon.getPath().clear();
        if (value != null && value.getPoints() != null) {
            value.getPoints().forEach(e -> gpolygon.addPath(new LatLng(e.getLat(), e.getLng())));
            points = value.getPoints();
        } else {
            points = new ArrayList<>();
        }
    }

    public void setDisabled(boolean bool) {
        if (bool) maps.addEventListener("onMapClick", x -> {
        });
    }

    public double getLat() {
        return maps.getLat();
    }

    public void setLat(double lat) {
        maps.setLat(lat);
    }

    public double getLng() {
        return maps.getLng();
    }

    public void setLng(double lng) {
        maps.setLng(lng);
    }
}

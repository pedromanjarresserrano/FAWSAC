package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.Point;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;

@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class PointGmap extends Gmaps {
    private transient @Getter
    Point value;
    private @Getter
    @Setter
    Gmarker location;
    private @Getter
    @Setter
    boolean disabled;

    public PointGmap() {
        super();
        location = new Gmarker();
        this.appendChild(location);
    }

    /**
     * @param value the value to set
     */
    public void setValue(Point value) {
        this.value = value;
        location.setLng(value.getLng());
        location.setLat(value.getLat());
        location.setContent("Location");
    }

}

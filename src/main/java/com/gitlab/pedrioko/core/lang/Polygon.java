package com.gitlab.pedrioko.core.lang;

import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.core.lang.annotation.UIEntity;
import com.gitlab.pedrioko.domain.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.StringJoiner;

@Entity
@UIEntity
public @Data
class Polygon extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "polygon_points", joinColumns = @JoinColumn(name = "poligon_id"), inverseJoinColumns = @JoinColumn(name = "point_id"))
    @NoEmpty
    private List<Point> points;
    @Version
    private int version;

    public String getStringPoints() {
        StringJoiner sj = new StringJoiner(",");
        if (points != null) points.forEach(xy -> {
            sj.add(String.valueOf(xy.getLat()));
            sj.add(String.valueOf(xy.getLng()));
            sj.add("3");
        });
        return sj.toString();
    }

}

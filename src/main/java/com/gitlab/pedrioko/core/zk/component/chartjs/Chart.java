package com.gitlab.pedrioko.core.zk.component.chartjs;

import com.gitlab.pedrioko.core.zk.component.chartjs.domain.DataChart;
import com.gitlab.pedrioko.core.zk.component.chartjs.domain.enums.ChartType;
import com.google.gson.Gson;
import lombok.Data;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;

public @Data
class Chart extends HtmlBasedComponent {
    private ChartType type;
    private DataChart data;
    private boolean showLegend = false;
    private boolean responsive = true;
    private boolean maintainAspectRatio = false;

    public Chart() {
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "_type", type != null ? type.toString() : "bar");
        render(renderer, "_data", new Gson().toJson(data));
        render(renderer, "_showLegend", showLegend);
        render(renderer, "_responsive", responsive);
        render(renderer, "_maintainAspectRatio", maintainAspectRatio);
    }

    public void setType(ChartType type) {
        this.type = type;
        smartUpdate("_type", type.toString());

    }

    public void setData(DataChart data) {
        this.data = data;
        smartUpdate("_data", new Gson().toJson(data));
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
        smartUpdate("_showlegend", showLegend);
    }

    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
        smartUpdate("_responsive", responsive);
    }

    public void setMaintainAspectRatio(boolean maintainAspectRatio) {
        this.maintainAspectRatio = maintainAspectRatio;
        smartUpdate("_maintainAspectRatio", maintainAspectRatio);
    }
}

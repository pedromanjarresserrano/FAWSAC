package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.zk.component.model.DataChart;
import com.gitlab.pedrioko.core.zk.component.model.enums.ChartType;
import lombok.Data;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;

import java.util.List;

public @Data
class Chart extends Canvas {

    private String title;
    private ChartType type;
    private List<DataChart> dataset;
    private Long borderWidth = 1L;

    private String backgroundColor;

    private String borderColor;
    private Canvas chartHD = new Canvas();
    private boolean showLegend = false;

    public Chart(String title, ChartType type, Long borderWidth) {
        super();
        this.title = title;
        this.type = type;
        this.borderWidth = borderWidth;
    }

    public Chart(String title, ChartType type) {
        super();
        this.title = title;
        this.type = type;
    }

    public void setDataset(List<DataChart> dataset) {
        this.dataset = dataset;
        chartHD.setStyle("width: 420px; height: 210px;");
        Div div = new Div();
        div.appendChild(chartHD);
        div.setStyle("width: 420px; height: 210px;");
        addEventListener(Events.ON_AFTER_SIZE, (e) -> {
            updateDataSet(dataset);
            if (div.getParent() == null)
                getParent().appendChild(div);
            showLegend = true;
            load(dataset, chartHD.getUuid());
            showLegend = false;
            div.setStyle("height:0; width:0; overflow:hidden;");
        });
    }

    public void updateDataSet(List<DataChart> dataset) {
        this.dataset = dataset;
        String id = getUuid();
        load(dataset, id);
    }

    private void load(List<DataChart> dataset, String id) {
        String labels = "";
        String data = "";
        String backgroundColor = "";
        String borderColor = "";
        for (int i = 0; i < dataset.size(); i++) {
            DataChart dataChart = dataset.get(i);
            labels += "\'" + dataChart.getLabel() + "\'" + (i == dataset.size() - 1 ? "" : ",");
            data += dataChart.getValue() + (i == dataset.size() - 1 ? "" : ",");
            backgroundColor += "\'" + dataChart.getBackgroundColor() + "\'" + (i == dataset.size() - 1 ? "" : ",");
            borderColor += "\'" + dataChart.getBorderColor() + "\'" + (i == dataset.size() - 1 ? "" : ",");

        }
        String script = "{\n" +
                "    type: \'" + type.toString() + "\',\n" +
                "    data: {\n" +
                "        labels: [" + labels + "],\n" +
                "        datasets: [{\n" +
                "            label: '" + title + "',\n" +
                "            data: [" + data + "],\n" +
                "            backgroundColor: [\n" + (backgroundColor.isEmpty() || type == ChartType.LINE ? "'" + this.backgroundColor + "'" : backgroundColor) +
                "            ],\n" +
                "            borderColor: [\n" + (borderColor.isEmpty() || type == ChartType.LINE ? "'" + this.borderColor + "'" : borderColor) +
                "            ],\n" +
                "            borderWidth: " + borderWidth + "\n" +
                "        }]\n" +
                "    }," +
                "    options: {\n" +

                "plugins: {\n" +
                "      datalabels: {\n" +
                (showLegend == true ? "" +
                        "             backgroundColor: function(context) {\n" +
                        "                return context.dataset.backgroundColor;\n" +
                        "             },\n" +
                        "             borderRadius: 4,\n" +
                        "             color: 'white',\n" +
                        "             font: {\n" +
                        "                 weight: 'bold'\n" +
                        "             },\n" +
                        "             formatter: Math.round\n" : "" +
                        "display: false") +
                "      }\n" +
                "}," +
                "        responsive: true,\n" +
                "        maintainAspectRatio: false,\n" +
                "        legend: {\n" +
                "            display: true,\n" +
                "            labels: {\n" +
                "                fontColor: 'rgb(255, 99, 132)'\n" +
                "            }\n" +
                "        }" +
                (type == ChartType.BAR ? ",\n" +
                        "        scales: {\n" +
                        "            yAxes: [{\n" +
                        "                ticks: {\n" +
                        "                    beginAtZero:true\n" +
                        "                }\n" +
                        "            }]\n" +
                        "        }\n" : "") +
                "    }\n" +
                "}";
        setStyle("width: 100%;");
        Clients.evalJavaScript("loadChart(\"" + id + "-cnt\"" + "," + script + ")");
    }

    public Canvas ChartHD() {

        return chartHD;
    }

    public void reload() {
        load(dataset, chartHD.getId());
    }
}

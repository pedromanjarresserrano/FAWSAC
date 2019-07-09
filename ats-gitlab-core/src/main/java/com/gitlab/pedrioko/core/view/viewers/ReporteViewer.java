package com.gitlab.pedrioko.core.view.viewers;

import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.Exporter;
import com.gitlab.pedrioko.core.view.util.StringUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.zk.component.chartjs.Chart;
import com.gitlab.pedrioko.core.zk.component.chartjs.domain.DataChart;
import com.gitlab.pedrioko.core.zk.component.chartjs.domain.DataChartSet;
import com.gitlab.pedrioko.core.zk.component.chartjs.domain.enums.ChartType;
import com.gitlab.pedrioko.domain.Grafico;
import com.gitlab.pedrioko.domain.Reporte;
import com.gitlab.pedrioko.services.CrudService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ReporteViewer extends Borderlayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReporteViewer.class);
    private final Listbox lb = new Listbox();
    private final Listhead columns = new Listhead();
    private final Toolbar toolbar = new Toolbar();
    private List<Object[]> data = new ArrayList<>();
    private final VelocityContext context = new VelocityContext();
    private final List<Chart> charts = new ArrayList<>();

    public ReporteViewer(Reporte reporte) {
        String sqLquery = reporte.getSQLquery();
        if (sqLquery != null && !sqLquery.isEmpty()) {
            if (sqLquery.toUpperCase().contains("DELETE") || sqLquery.toUpperCase().contains("UPDATE") || sqLquery.toUpperCase().contains("INSERT") || !sqLquery.toUpperCase().contains("SELECT")) {
                ZKUtil.showMessage(ReflectionZKUtil.getLabel("sqlinvalido"));
                return;
            }
        }
        appendChild(new North());
        appendChild(new East());
        appendChild(new Center());
        getNorth().appendChild(toolbar);
        Toolbarbutton child = new Toolbarbutton();
        child.setTooltip(ReflectionZKUtil.getLabel("export") + " Excel");
        child.setSclass("btn-toolbar");
        child.setStyle("background:#27AE60;");
        child.setIconSclass("fa  fa-file-excel fa-lg");
        child.addEventListener(Events.ON_CLICK, e -> {
            String filename = "export-" + UUID.randomUUID() + ".xls";
            try {
                byte[] buildExcel = Exporter.BuildExcel(data, reporte.getColumnas());
                if (buildExcel == null) {
                    ZKUtil.showMessage(ReflectionZKUtil.getLabel("emptyexport"), MessageType.INFO);
                    return;
                }
                Filedownload.save(new ByteArrayInputStream(buildExcel), "", filename);
            } catch (Exception ex) {
                LOGGER.error("ERROR on actionPerform()", ex);
            }
        });
        toolbar.setWidth("100%");
        toolbar.appendChild(child);
        child = new Toolbarbutton();
        child.setTooltip(ReflectionZKUtil.getLabel("export") + " PDF");
        child.setSclass("btn-toolbar");
        child.setStyle("background:#FF0000;");
        child.setIconSclass("fas  fa-file-pdf fa-lg");
        child.addEventListener(Events.ON_CLICK, e -> {
         /*   String heads = new Gson().toJson(reporte.getColumnas());
            String datajson = new Gson().toJson(data);
            StringBuilder chartstring = new StringBuilder();
            chartstring.append("var ycord = splitTitle.length * lineHeight +110; ");

            charts.forEach(c -> {
                String uuid = c.getChartHD().getUuid();
                chartstring.append("" +
                        "console.log(ycord);" +
                        "if(ycord > ( height * 0.85 )){" +
                        "doc.addPage(\"a4\",\"p\");\n" +
                        "ycord = 50;" +
                        "} " +
                        "var canvas" + uuid + " = document.getElementById(\"" + uuid + "-cnt\");  " +
                        " var newCan" + uuid + " = canvas" + uuid + ".cloneNode(true);\n" +
                        "  var ctx" + uuid + " = newCan" + uuid + ".getContext('2d');\n" +
                        "  ctx" + uuid + ".fillStyle = \"#FFF\";\n" +
                        "  ctx" + uuid + ".fillRect(0, 0, newCan" + uuid + ".width, newCan" + uuid + ".height);\n" +
                        "  ctx" + uuid + ".drawImage(canvas" + uuid + ", 0, 0);  " +
                        "var link" + uuid + " = newCan" + uuid + ".toDataURL(\"image/jpeg\");" +
                        "doc.addImage(link" + uuid + ", 'JPEG', (width / 3.5), ycord, 280 , 130);" +
                        "ycord = ycord + newCan" + uuid + ".height;");
            });
            String s = chartstring.toString();
            Clients.evalJavaScript("" +
                    "var columns = " + heads + ";\n" +
                    "var rows = " + datajson + ";" +
                    "var doc = new jsPDF('p', 'pt');\n" +
                    "var width = doc.internal.pageSize.getWidth(); " +
                    "var height = doc.internal.pageSize.getHeight(); " +
                    "doc.setFontSize(40);\n" +
                    "doc.text(40, 70, \"" + reporte.getNombre() + "\");" +
                    "doc.setFontSize(12);\n" +
                    "var splitTitle = doc.splitTextToSize(\"" + reporte.getDescripcion() + "\", width-80 );\n" +
                    "var lineHeight = doc.getLineHeight(\"" + reporte.getDescripcion() + "\") / doc.internal.scaleFactor\n" +
                    "console.log(splitTitle);" +
                    "console.log(lineHeight);" +
                    "doc.text(40, 110,splitTitle    );" +
                    s +
                    "doc.addPage(\"a4\",\"p\");\n" +
                    "doc.autoTable(columns, rows);\n" +
                    "doc.save('table.pdf');");
*/
        });
        toolbar.appendChild(child);

        columns.setParent(lb);
        List<String> columnas = reporte.getColumnas();
        if (columnas != null) columnas.forEach(e -> {
            Listheader column = new Listheader();
            column.setLabel(ReflectionZKUtil.getLabel(e));
            column.setParent(columns);
            column.setHflex("max");
        });
        lb.setWidth("100%");

        getCenter().appendChild(lb);
        getCenter().setStyle("overflow-y:auto;");
        String query = sqLquery;
        loadData(query);

        loadCharts(reporte);

    }

    private void loadCharts(Reporte reporte) {
        List<Grafico> graficos = reporte.getGraficos();
        if (graficos != null && !graficos.isEmpty()) {
            East east = getEast();
            Vlayout child = new Vlayout();
            east.appendChild(child);
            List<String> columnas = reporte.getColumnas();
            east.setSclass("chart-view");
            graficos.forEach(e -> {
                int indexlabels = columnas.indexOf(e.getColumnaetiquetas());
                int indexdata = columnas.indexOf(e.getColumnavalores());
                Chart chart = new Chart();
                chart.setType(ChartType.valueOf(e.getTipo().name()));
                List<DataChart> dataset = new ArrayList<>();
                List<String> labels = new ArrayList<>();
                DataChart dataChart = new DataChart();
                DataChartSet chartSet = new DataChartSet();
                ArrayList<String> values = new ArrayList<>();
                for (Object[] rowdata : data) {
                    labels.add(rowdata[indexlabels].toString());
                    values.add(rowdata[indexdata].toString());
                }
                chartSet.setBorderColor(StringUtil.getRandomHexColor());
                chartSet.setData(values);
                dataChart.setLabels(labels);
                dataChart.setDatasets(Arrays.asList(chartSet));
                chart.setData(dataChart);
                child.appendChild(chart);
                charts.add(chart);
            });
        }

    }

    public void loadData(String query) {
        StringWriter swOut = new StringWriter();
        Velocity.evaluate(context, swOut, "ReportLog", query);
        data = ApplicationContextUtils.getBean(CrudService.class).getEntityByQuery(swOut.toString());
        loadData(data);
    }

    public void loadData(List<Object[]> entityByQuery1) {
        for (Object[] rowdata : entityByQuery1) {
            Listitem row = new Listitem();
            for (int i = 0; i < rowdata.length; i++) {
                Listcell cell = new Listcell();
                Label label = new Label();
                label.setValue(String.valueOf(rowdata[i]));
                cell.appendChild(label);
                row.appendChild(cell);
            }
            row.setValue(rowdata);
            lb.appendChild(row);
        }
    }
}
package com.gitlab.pedrioko.zk.vm.user;

import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.zk.component.chartjs.Chart;
import com.gitlab.pedrioko.core.zk.component.chartjs.domain.DataChart;
import com.gitlab.pedrioko.core.zk.component.chartjs.domain.DataChartSet;
import com.gitlab.pedrioko.core.zk.component.chartjs.domain.enums.ChartType;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class Dashboard.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Dashboard extends SelectorComposer<Window> {

    private static final long serialVersionUID = 1L;


    @WireVariable
    private transient FHSessionUtil fhsessionutil;
    @Wire
    private Div content;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.zkoss.zk.ui.util.Composer#doAfterCompose(org.zkoss.zk.ui.Component)
     */
    @Override
    public void doAfterCompose(Window window) throws Exception {
        super.doAfterCompose(window);
        Chart child = new Chart();
        DataChart data = new DataChart();
        List<String> labels = new ArrayList<>();
        labels.add("datalabels");
        labels.add("datalabels 2");
        labels.add("datalabels 3");
        labels.add("datalabels 4");
        labels.add("datalabels 5");
        data.setLabels(labels);
        List<DataChartSet> datasets = new ArrayList<>();
        DataChartSet chartSet = new DataChartSet();
        chartSet.setBackgroundColor("rgba(0,0,0,0.0)");
        chartSet.setBorderColor("#000");
        chartSet.setBorderWidth(1);
        chartSet.setLabel("test Datas");
        ArrayList<String> values = new ArrayList<>();
        values.add("1");
        values.add("2");
        values.add("2");
        values.add("2");
        values.add("4");
        values.add("2");
        chartSet.setData(values);
        datasets.add(chartSet);
        chartSet = new DataChartSet();
        chartSet.setBackgroundColor("rgba(0,0,0,0.0)");
        chartSet.setBorderColor("#4fd");
        chartSet.setBorderWidth(1);
        chartSet.setLabel("test Datas");
        values = new ArrayList<>();
        values.add("11");
        values.add("22");
        values.add("33");
        values.add("44");
        values.add("22");
        values.add("11");
        chartSet.setData(values);
        datasets.add(chartSet);
        data.setDatasets(datasets);
        child.setData(data);
        child.setResponsive(false);
        child.setType(ChartType.LINE);
        child.setHeight("200px !important");
        Div div = new  Div();
        div.appendChild(child);
        div.setHeight("200px !important");

        window.appendChild(div);
        List<DashBoardComponent> listView = ApplicationContextUtils
                .getBeansOfType(DashBoardComponent.class)
                .stream()
                .sorted(Comparator.comparingInt(DashBoardComponent::getPosicion))
                .collect(Collectors.toList());

        for (DashBoardComponent e : listView) {
            List<TipoUsuario> collect = Arrays.asList(e.getForUser());

            if (!collect.isEmpty() && (collect.contains(TipoUsuario.ALL)
                    || collect.contains(fhsessionutil.getCurrentUser().getTipo()))) {
                Panel content = e.getContent();
                if (e.haveViewMore()) {
                    Button viewmore = new Button("View More");
                    viewmore.setSclass("pull-right btn-success");
                    viewmore.addEventListener(Events.ON_CLICK, w -> {
                        Window windowviewmore = (Window) Executions.createComponents(e.urlViewMore(), null, null);
                        windowviewmore.setSclass("col-md-10");
                        windowviewmore.setBorder("normal");
                        windowviewmore.setWidth("70%");
                        windowviewmore.setHeight("80%");
                        windowviewmore.setTitle(e.getLabel());
                        windowviewmore.setClosable(true);
                        windowviewmore.doModal();
                    });
                    viewmore.setStyle("margin-bottom:10px; margin-right:10px;");
                    content.getChildren().get(0).appendChild(viewmore);
                }
                content.setStyle("margin-bottom:10px;");
                content.setCollapsible(true);
                content.setTitle(e.getLabel());
                content.setClosable(true);
                content.setBorder("normal");
                content.getChildren().forEach(w -> w.setId(""));
                content.setSclass("col-lg-6 col-md-6 col-sm-12");
                this.content.appendChild(content);
            }
        }
    }

    public boolean isMobile() {
        return Executions.getCurrent().getBrowser("mobile") != null;
    }
}

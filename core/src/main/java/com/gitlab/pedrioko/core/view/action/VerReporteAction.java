package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.api.ContentView;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.ReporteViewer;
import com.gitlab.pedrioko.core.zk.component.rangebox.DateRangeBox;
import com.gitlab.pedrioko.core.zk.component.window.Window;
import com.gitlab.pedrioko.domain.Reporte;
import com.gitlab.pedrioko.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Space;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@Component
@Order(0)
public class VerReporteAction implements Action {


    private final static String VER = "Ver";
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    @Autowired
    private CrudService crudService;

    @Override
    public String getIcon() {
        return "fas  fa-database";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        ContentView contentView = ApplicationContextUtils.getBean(ContentView.class);
        Reporte value1 = (Reporte) event.getValue();
        if (value1 == null) {
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("seleccione"), MessageType.WARNING);
        } else {
            if (value1.getUsaRangoFecha()) {
                Window form = new Window();
                DateRangeBox dateRangeBox = new DateRangeBox();
                form.appendChild(new Label(ReflectionZKUtil.getLabel("rangofechas")));
                form.appendChild(new Space());
                form.appendChild(dateRangeBox);
                form.appendChild(new Space());
                Button b = new Button();
                b.setLabel(ReflectionZKUtil.getLabel(VER));
                b.setIconSclass("fa fa-info");
                b.setSclass("btn btn-primary");
                b.addEventListener(Events.ON_CLICK, e -> {
                    if (dateRangeBox.getValue() != null && dateRangeBox.getValue().getInicio() != null && dateRangeBox.getValue().getFin() != null) {
                        String s = value1.getSQLquery().replaceFirst("\\?", df.format(dateRangeBox.getValue().getInicio()));
                        s = s.replaceFirst("\\?", df.format(dateRangeBox.getValue().getFin()));
                        value1.setSQLquery(s);
                        contentView.closeCurrent();
                        ReporteViewer win = new ReporteViewer(value1);
                        contentView.addView(win, value1.getNombre() + value1.getId(), value1.getNombre());
                    } else ZKUtil.showMessage(ReflectionZKUtil.getLabel("emptyrangofechas"), MessageType.INFO);
                });
                Hlayout h = new Hlayout();
                h.appendChild(b);
                b = new Button();
                b.setLabel(ReflectionZKUtil.getLabel("cancelar"));
                b.setIconSclass("fa fa-ban");
                b.setSclass("btn btn-danger");
                b.addEventListener(Events.ON_CLICK, e -> contentView.closeCurrent());
                h.appendChild(b);
                h.setWidth("100%");

                form.setClass("col-sm-12 col-md-6 col-lg-6");
                form.appendChild(h);
                contentView.addView(form, value1.getNombre() + value1.getId(), value1.getNombre());
            } else {
                ReporteViewer win = new ReporteViewer(value1);
                contentView.addView(win, value1.getNombre() + value1.getId(), value1.getNombre());
            }

        }
    }


    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(Reporte.class);
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getClasses() {
        return "btn-info";
    }

    @Override
    public FormStates getFormState() {
        return FormStates.READ;
    }

    @Override
    public Integer position() {
        return 1;
    }

    @Override
    public String getColor() {
        return "#4bcffa";
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel(VER);
    }

}
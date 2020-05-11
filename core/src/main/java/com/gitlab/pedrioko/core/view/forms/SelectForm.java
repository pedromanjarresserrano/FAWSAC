package com.gitlab.pedrioko.core.view.forms;

import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.services.CrudService;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import java.util.List;


public class SelectForm extends Window {

    private static final long serialVersionUID = 1L;
    private final CrudService crudService;
    private final Div actions;
    private final Combobox combobox;

    public SelectForm(Class klass) {
        setTitle("Seleccione " + klass.getSimpleName());
        crudService = ApplicationContextUtils.getBean(CrudService.class);
        List allOrder = crudService.getAllOrder(klass);
        combobox = new Combobox();
        ReflectionZKUtil.populate(combobox, allOrder, false);
        combobox.setSclass("col-md-12 col-lg-12 col-xs-12 col-sm-12");
        appendChild(combobox);
        actions = new Div();
        actions.setSclass("col-md-12 col-lg-12 col-xs-12 col-sm-12");
        appendChild(actions);

    }

    public void addAction(String labelaction, String icon, String classes, EventListener<? extends Event> event) {
        org.zkoss.zul.Button btn = new Button();
        btn.setLabel(labelaction);
        btn.setIconSclass(icon);
        btn.setClass("btn-action " + classes + (ZKUtil.isMobile() ? " col-sm-12 " : ""));
        btn.addEventListener(Events.ON_CLICK, event);
        actions.appendChild(btn);
    }

    public <T> T getValue() {
        return (T) combobox.getSelectedItem().getValue();
    }
}

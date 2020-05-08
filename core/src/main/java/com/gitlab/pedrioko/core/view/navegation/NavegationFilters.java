package com.gitlab.pedrioko.core.view.navegation;

import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.CrudService;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Vlayout;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NavegationFilters extends Vlayout {
    private String[] classes;
    private final CrudService crudService;
    private List<Groupbox> groups = new LinkedList<>();
    private List<Checkbox> checks = new LinkedList<>();

    public NavegationFilters() {
        crudService = ApplicationContextUtils.getBean(CrudService.class);
    }

    public NavegationFilters(String[] classes) {
        crudService = ApplicationContextUtils.getBean(CrudService.class);
        init(classes);
    }

    protected void init(String[] classes) {
        List<String> strings = Arrays.asList(classes);
        strings.forEach(e -> {
            try {
                Class<?> aClass = Class.forName(e);
                Groupbox groupbox = getGroupbox(aClass.getSimpleName());
                groups.add(groupbox);
                List<?> allOrder = crudService.getAllOrder(aClass);
                Vlayout div = new Vlayout();
                allOrder.forEach(obj -> {
                    Checkbox checkbox = new Checkbox();
                    checkbox.setWidth("100%");
                    checkbox.setValue(obj);
                    checkbox.setLabel("  " + obj.toString());
                    checks.add(checkbox);
                    div.appendChild(checkbox);
                });
                groupbox.appendChild(div);
                appendChild(groupbox);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }

        });
    }

    public String getClasses() {
        return classes.toString();
    }

    public void setClasses(String classes) {
        this.classes = classes.split(":");
        init(this.classes);
    }

    private Groupbox getGroupbox(String k) {
        Groupbox gb = new Groupbox();
        gb.setId(k);
        gb.setMold("3d");
        Caption c = new Caption(ReflectionZKUtil.getLabel(k));
        c.setId("caption" + k);
        c.setIconSclass("fa fa-chevron-down pull-right");
        gb.getChildren().add(c);
        gb.setOpen(true);
        return gb;
    }

    @Override
    public boolean addEventListener(String evtnm, EventListener<? extends Event> listener) {
        checks.forEach(e -> e.addEventListener(evtnm, listener));
        return super.addEventListener(evtnm, listener);
    }
}

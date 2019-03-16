package com.gitlab.pedrioko.core.view.viewers.crud;

import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudMode;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.controllers.CrudController;
import com.gitlab.pedrioko.core.view.viewers.crud.grid.AlphabetFilter;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean;

public class CrudView extends Tabpanel {

    private static final long serialVersionUID = 1L;
    private Class<?> klass;
    //    private @Getter
//    CrudTable crudTable;
//    private @Getter
//    CrudGrid gridTable;
    private List<Button> listActions;
    private Div divbar;
    private @Getter
    @Setter
    Div actions;
    private List<Component> previusChilderns;
    //  private CrudViewBar toolbar;

    private @Getter
    @Setter
    Boolean reloadable;

    private @Getter
    @Setter
    CrudController crudController;
    private @Getter
    @Setter
    CrudMode crudviewmode;
    private @Getter
    East east;
    private CrudMenuContext popup;
    private North north;
    //  private CrudFilters crudFilters;
    private Borderlayout borderlayout;
    private int PAGE_SIZE = 16;
    //  private Paging paging;

    public CrudView() {
    }

    public CrudView(Class<?> klass) {
        super();
        crudviewmode = CrudMode.MAINCRUD;
        view(klass, getBean(PropertiesUtil.class).getFieldTable(klass));

    }

    public CrudView(Class<?> klass, CrudMode crudviewmode) {
        super();
        this.crudviewmode = crudviewmode;
        view(klass, getBean(PropertiesUtil.class).getFieldTable(klass));
    }

    public CrudView(Class<?> klass, Boolean useGrid) {
        super();
        init(klass, useGrid);
    }

    public void useAlphabetFilter() {
        North north = new North();
        north.appendChild(new AlphabetFilter(crudController, klass));

    }

    protected void init(Class<?> klass, Boolean useGrid) {
        crudviewmode = CrudMode.MAINCRUD;
        this.klass = klass;
        createUI();
        crudController.doQuery();
        popup = new CrudMenuContext(klass, ApplicationContextUtils.getBeans(Action.class));
        appendChild(popup);
    }

    public void setPageSize(int PAGE_SIZE) {
        this.PAGE_SIZE = PAGE_SIZE;
        crudController.setPage(PAGE_SIZE, PAGE_SIZE);
        crudController.doQuery();
    }

    private void createUI() {
        borderlayout = new Borderlayout();
        divbar = new Div();
        actions = new Div();
        HashMap<Object, Object> arg = new HashMap<>();
        arg.put("klass-crud", klass);
        arg.put("CrudView", this);

        Component crudviewbar = Executions.createComponents("~./zul/crud/crudviewbar.zul", null, arg);
        divbar.appendChild(crudviewbar);
        borderlayout.setStyle("height:100%;");
        east = new East();
        north = new North();
        north.appendChild(divbar);
        borderlayout.appendChild(north);
        east.setTitle("Filters");
        east.setStyle(" overflow-y:auto !important; width:350px;");
        if (ZKUtil.isMobile()) {
            east.setCollapsible(false);
            east.setSplittable(false);
            east.setOpen(false);
            east.setSlide(true);
        } else {
            east.setSplittable(false);
            east.setCollapsible(true);
            east.setOpen(false);
            east.setVisible(true);
            east.setSlide(false);
        }
        borderlayout.appendChild(east);
        borderlayout.appendChild(new Center());
        appendChild(borderlayout);
        Center center = borderlayout.getCenter();
        configController(klass);
        arg.put("crud-list-items", crudController.getValues());
        arg.put("crud-controller", crudController);
        Component crudtable = null;
        try {
            crudtable = Executions.createComponents("~./zul/crud/table/crudtable" + klass.getSimpleName() + ".zul", null, arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (crudtable == null) crudtable = Executions.createComponents("~./zul/crud/table/crudtable.zul", null, arg);

        center.appendChild(crudtable);
        Component crudfilters = Executions.createComponents("~./zul/crud/filters/crudfilters.zul", null, arg);
        east.appendChild(crudfilters);
        appendChild(actions);
        actions.setClass("col-md-12 col-lg-12 col-xs-12 col-sm-12");
        actions.setStyle("margin-top:10px;margin-bottom:10px;");
        setStyle("height:100%;");
        reloadable = crudviewmode != CrudMode.SUBCRUD;
        east.addEventListener(Events.ON_VISIBILITY_CHANGE, e -> {
            if (!east.isVisible()) {
                clearParams();
                crudController.doQuery();
            }
        });
        South south = new South();
        Component pagination = Executions.createComponents("~./zul/crud/filters/pagination.zul", null, arg);
        south.appendChild(pagination);
        borderlayout.appendChild(south);

    }

    public CrudView(Class<?> klass, Object value) {
        super();
        crudviewmode = CrudMode.MAINCRUD;
        this.klass = klass;
        crudController = new CrudController(klass, (List<?>) value);
        createUI();
    }

    private void view(Class<?> klass, List<String> fields) {
        this.klass = klass;
        listActions = new ArrayList<>();
        createUI();
    }

    public Class<?> getTypeClass() {
        return klass;
    }

    private void configController(Class<?> klass) {
        if (crudController == null) {
            crudController = new CrudController(klass);
            crudController.doQuery();
        }
    }

    public void previusState() {
        getChildren().clear();
        createUI();
        getTabbox().setSelectedIndex(0);
        if (reloadable)
            update();
    }

    public List<String> getValueIds() {
        return crudController.getValueIds();
    }

    public void setValueIds(List<String> value) {
        crudController.setValueIds(value);
    }

    public void setContent(Component c) {
        previusChilderns = new ArrayList<>(getChildren());
        getChildren().clear();
        appendChild(c);
    }

    public <T> T getValue() {
        return (T) crudController.getFullData();
    }

    public void setValue(List<?> value) {
        crudController.setValue(value);
    }

    public void setValue(ArrayList<?> value) {
        crudController.setValue(value);
    }

    public List<Button> getListActions() {
        return listActions;
    }

    public void setListActions(List<Button> listActions) {
        this.listActions = listActions;
    }

    public Div getToolbar() {
        return divbar;
    }

    public void setToolbar(Div toolbar) {
        divbar = toolbar;
    }

    public void setDisabled(boolean disable) {
        if (disable)
            divbar.setVisible(!disable);
    }

    public String getKlass() {
        return klass.getName();
    }

    public void setKlass(String klass) throws ClassNotFoundException {
        String[] split = klass.split(":");
        if (split.length > 1) {
            String s = split[0];
            Class<?> aClass = Class.forName(s);
            this.klass = aClass;
            boolean aBoolean = new Boolean(split[1]);
            init(aClass, aBoolean);
        } else {
            if (split.length > 3) {
                String s = split[0];
                Class<?> aClass = Class.forName(s);
                this.klass = aClass;
                crudviewmode = CrudMode.MAINCRUD;
                view(aClass, getBean(PropertiesUtil.class).getFieldTable(aClass));
            }
        }
    }

/*
    public void enableCommonCrudActions(boolean disable) {
        toolbar.getCrudsActions().forEach(e -> e.setVisible(disable));
    }
*/

    public void addValue(Object value) {
        crudController.addValue(value);
    }

    public void addAction(Action action, CrudActionEvent event) {
        addAction(action.getLabel(), action.getIcon(), "btn " + action.getClasses(), e -> {
            EventQueues.lookup("action-crud-" + klass.getSimpleName(), EventQueues.SESSION, true).publish(new Event("action-crud-" + klass.getSimpleName() + "-" + UUID.randomUUID().toString(), null, action));
        });
    }

    public void addAction(String labelaction, String icon, EventListener<? extends Event> event) {
        addAction(labelaction, icon, "btn btn-primary pull-left", event);
    }

    public void addAction(String labelaction, String icon, String classes, EventListener<? extends Event> event) {
        Button btn = new Button();
        btn.setLabel(labelaction);
        btn.setIconSclass(icon);
        btn.setClass("btn btn-action " + classes);
        btn.addEventListener(Events.ON_CLICK, event);
        actions.appendChild(btn);

    }

    public void update() {
        BindUtils.postGlobalCommand(null, null, "refresh", null);
    }

    public void addRootParams(String key, Object value) {
        crudController.putRoot(key, value);
    }

    public void getRootParams(String key) {
        crudController.getRoot(key);
    }


    void addParams(String key, Object value) {
        crudController.put(key, value);
    }

    public void getParams(String key) {
        crudController.get(key);
    }

    void clearParams() {
        crudController.clearParams();
    }

}

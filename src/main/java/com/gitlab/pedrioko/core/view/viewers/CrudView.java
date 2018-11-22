package com.gitlab.pedrioko.core.view.viewers;

import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.controllers.CrudController;
import com.gitlab.pedrioko.core.view.enums.CrudEvents;
import com.gitlab.pedrioko.core.view.enums.CrudMode;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.List;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean;

public class CrudView extends Tabpanel {

    private static final long serialVersionUID = 1L;
    private Class<?> klass;
    private @Getter
    CrudTable crudTable;
    private @Getter
    CrudGrid gridTable;
    private List<Button> listActions;
    private Div divbar;
    private @Getter
    @Setter
    Div actions;
    private List<Component> previusChilderns;
    private CrudViewBar toolbar;

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
        crudviewmode = CrudMode.MAINCRUD;
        this.klass = klass;
        gridTable = new CrudGrid(klass);
        Borderlayout child = new Borderlayout();
        this.klass = klass;
        divbar = new Div();
        actions = new Div();
        toolbar = new CrudViewBar(this.klass, this, this.gridTable);
        divbar.appendChild(toolbar);
        appendChild(divbar);
        child.setStyle("height:95%;");
        east = new East();
        child.appendChild(new North());
        east.setCollapsible(true);
        east.setTitle("Filters");
        east.setVisible(false);
        east.setStyle(" overflow-y:auto !important; width:350px;");
        child.appendChild(east);
        child.appendChild(new Center());
        appendChild(child);
        Center center = child.getCenter();
        center.appendChild(gridTable);
//        center.setHeight("95%");
        child.getEast().appendChild(new CrudFilters(klass, this));
//        appendChild(crudTable);
        appendChild(actions);
        actions.setClass("col-md-12 col-lg-12 col-xs-12 col-sm-12");
        actions.setStyle("margin-top:10px;margin-bottom:10px;");
        setStyle("height:100%;");
        reloadable = crudviewmode != CrudMode.SUBCRUD;
        popup = new CrudMenuContext(klass, ApplicationContextUtils.getBeans(Action.class));
        this.appendChild(popup);
        gridTable.addEventOnEvent(CrudEvents.ON_RIGHT_CLICK, () -> popup.open(gridTable.getRows(), "at_pointer", gridTable.getSelectedValue()));
        crudController = new CrudController(klass, gridTable.getValue());
        crudController.addEventPostQuery(() -> gridTable.update());
        crudController.doQuery();
        crudController.addEventOnEvent(CrudEvents.ON_ADD, () -> gridTable.update());
    }

    public CrudView(Class<?> klass, Object obj) {
        super();
        crudviewmode = CrudMode.MAINCRUD;
        this.klass = klass;
        crudTable = new CrudTable(klass, (List<Class<?>>) obj);
        crudController = new CrudController(klass, crudTable.getValue());
        create(klass, crudTable);
    }

    private void view(Class<?> klass, List<String> fields) {

        this.klass = klass;
        listActions = new ArrayList<>();
        divbar = new Div();
        crudTable = new CrudTable(fields, klass);
        create(klass, crudTable);
    }

    public Class<?> getTypeClass() {
        return klass;
    }

    private void create(Class<?> klass, CrudTable crudTable) {
        Borderlayout child = new Borderlayout();
        this.crudTable = crudTable;
        this.klass = klass;
        divbar = new Div();
        actions = new Div();
        toolbar = new CrudViewBar(this.klass, this, this.crudTable);
        divbar.appendChild(toolbar);
        appendChild(divbar);
        child.setStyle("height:95%;");
        east = new East();
        child.appendChild(new North());
        east.setCollapsible(true);
        east.setTitle("Filters");
        east.setVisible(false);
        east.setStyle(" overflow-y:auto !important; width:350px;");
        child.appendChild(east);
        child.appendChild(new Center());
        appendChild(child);
        Center center = child.getCenter();
        center.appendChild(crudTable);
//        center.setHeight("95%");
        child.getEast().appendChild(new CrudFilters(klass, this));
//        appendChild(crudTable);
        appendChild(actions);
        actions.setClass("col-md-12 col-lg-12 col-xs-12 col-sm-12");
        actions.setStyle("margin-top:10px;margin-bottom:10px;");
        setStyle("height:100%;");
        reloadable = crudviewmode != CrudMode.SUBCRUD;
        if (crudController == null) {
            crudController = new CrudController(klass, crudTable.getValue());
            crudController.addEventPostQuery(() -> crudTable.update());
            crudController.doQuery();
            crudController.addEventOnEvent(CrudEvents.ON_ADD, () -> crudTable.update());
        }
    }

    public void previusState() {
        if (previusChilderns != null && !previusChilderns.isEmpty()) {
            getChildren().clear();
            previusChilderns.forEach(this::appendChild);
        }
        if (reloadable)
            crudTable.update();
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
        return (T) ((T) crudTable == null ? gridTable.getValue() : crudTable.getValue());
    }

    public void setValue(List<?> value) {
        crudController.setValue(value);
        if (gridTable != null)
            gridTable.clearSelecion();
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

    public void enableCommonCrudActions(boolean disable) {
        toolbar.getCrudsActions().forEach(e -> e.setVisible(disable));
    }


    public void addValue(Object value) {
        crudController.addValue(value);
    }

    public void addAction(Action action, CrudActionEvent event) {
        addAction(action.getLabel(), action.getIcon(), "btn " + action.getClasses(), e -> {
            event.setValue(getSelectedValue());
            action.actionPerform(event);
        });
    }

    public void addAction(String labelaction, String icon, EventListener<? extends Event> event) {
        addAction(labelaction, icon, "btn btn-primary pull-left", event);
    }

    public void addAction(String labelaction, String icon, String classes, EventListener<? extends Event> event) {
        //      if (actions.getChildren().isEmpty())
//            setHeightTable("100%");
        Button btn = new Button();
        btn.setLabel(labelaction);
        btn.setIconSclass(icon);
        btn.setClass("btn btn-action " + classes);
        btn.addEventListener(Events.ON_CLICK, event);
        actions.appendChild(btn);

    }

    public void update() {
        crudTable.update();
    }

    /**
     * @return
     * @see CrudTable#getSelectedValue()
     */
    private <T> T getSelectedValue() {
        return crudTable.getSelectedValue();
    }

    /**
     * @param actions
     * @see CrudViewBar#onlyEnable(java.util.List)
     */
    public void onlyEnable(List<String> actions) {
        toolbar.onlyEnable(actions);
    }

    public void setHeightTable(String height) {
        crudTable.setHeight(height);
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

    public void enableAction(Class<? extends Action> action, boolean enable) {
        toolbar.enableAction(action, enable);
    }
}

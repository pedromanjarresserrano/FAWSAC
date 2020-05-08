package com.gitlab.pedrioko.core.view.viewers.crud;

import com.gitlab.pedrioko.core.view.api.OnEvent;
import com.gitlab.pedrioko.core.view.api.OnQuery;
import com.gitlab.pedrioko.core.view.enums.CrudEvents;
import com.gitlab.pedrioko.core.view.enums.CrudMode;
import com.gitlab.pedrioko.core.view.enums.ParamMode;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.controllers.CrudController;
import com.gitlab.pedrioko.core.view.viewers.crud.controllers.model.OrderBY;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tabpanel;

import java.util.*;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean;

public class CrudView extends Tabpanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudView.class);

    private static final long serialVersionUID = 1L;
    private Map<String, Object> rootParams = new LinkedHashMap<>();
    private Class<?> klass;
    private Div divbar;


    private @Getter
    @Setter
    Boolean reloadable = Boolean.FALSE;

    private @Getter
    @Setter
    CrudController crudController;
    private @Getter
    @Setter
    CrudMode crudviewmode;

    private int selectedIndex = 0;

    private int pagesize = 16;
    private HashMap<Object, Object> arg;
    private @Getter
    @Setter
    String crudViewUUID = "";

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

    public CrudView(Class<?> klass, int pagesize) {
        this.pagesize = pagesize;
        crudviewmode = CrudMode.MAINCRUD;
        view(klass, getBean(PropertiesUtil.class).getFieldTable(klass));

    }

    public CrudView(Class<?> klass, int pagesize, Map<String, Object> rootParams) {
        if (rootParams == null)
            throw new IllegalArgumentException("rootParams can't be null");
        this.rootParams = rootParams;
        this.pagesize = pagesize;
        crudviewmode = CrudMode.MAINCRUD;
        view(klass, getBean(PropertiesUtil.class).getFieldTable(klass));
    }
/*
    public void useAlphabetFilter() {
        North north = new North();
        north.appendChild(new AlphabetFilter(crudController, klass));

    }*/
/*
    protected void init(Class<?> klass) {
        crudviewmode = CrudMode.MAINCRUD;
        this.klass = klass;
        createUI();
        crudController.doQuery();
        popup = new CrudMenuContext(klass, ApplicationContextUtils.getBeans(Action.class));
        appendChild(popup);
    }*/

    public void setPageSize(int PAGE_SIZE) {
        crudController.setPageSize(PAGE_SIZE);
    }

    private void createUI() {
        divbar = new Div();
        arg = new HashMap<>();
        arg.put("klass-crud", klass);
        arg.put("CrudView", this);
        crudViewUUID = UUID.randomUUID().toString();
        arg.put("CrudViewUUID", crudViewUUID);
        configController(klass);
        arg.put("crud-list-items", crudController.getValues());
        arg.put("crud-controller", crudController);
        Component component = null;

        try {
            component = Executions.createComponents("~./zul/crud/crudview" + klass.getSimpleName() + ".zul", null, arg);
        } catch (Exception e) {
            LOGGER.info("CUSTOM CRUD TABLE PAGE NOT FOUND....");
            LOGGER.info("USING DEFAULT CRUD TABLE PAGE ");
        }
        if (component == null)
            component = Executions.createComponents("~./zul/crud/crudview.zul", null, arg);

        appendChild(component);
    }

    private Component loadPlantilla(Component component, String plantilla) {
        try {
            component = Executions.createComponents(plantilla + klass.getSimpleName() + ".zul", null, arg);
        } catch (Exception e) {
            LOGGER.info("CUSTOM CRUD TABLE PAGE NOT FOUND....");
            LOGGER.info("USING DEFAULT CRUD TABLE PAGE ");
        }
        if (component == null)
            component = Executions.createComponents(plantilla + ".zul", null, arg);
        return component;
    }

    public CrudView(Class<?> klass, List<?> value) {
        super();
        crudviewmode = CrudMode.MAINCRUD;
        this.klass = klass;
        crudController = new CrudController(klass, value);
        createUI();
    }

    public void openFilters() {
        EventQueues.lookup("filter-crud-" + getTypeClass().getSimpleName(), EventQueues.SESSION, true).publish(new Event("filter-crud-" + getTypeClass().getSimpleName() + "-" + getCrudViewUUID(), this, this));
    }

    private void view(Class<?> klass, List<String> fields) {
        this.klass = klass;
        createUI();
    }

    public Class<?> getTypeClass() {
        return klass;
    }

    private void configController(Class<?> klass) {
        if (crudController == null) {
            crudController = new CrudController(klass);
            crudController.putAllRoot(this.rootParams);
            crudController.setPageSize(this.pagesize);

        } else {
            crudController.setPage(0);
        }
    }

    public void previousState() {
        crudController.clearParams();
        getChildren().clear();
        createUI();
        getTabbox().setSelectedIndex(selectedIndex);
        if (reloadable!=null && reloadable)
            update();
    }
/*
    public List<String> getValueIds() {
        return crudController.getValueIds();
    }

    public void setValueIds(List<String> value) {
        crudController.setValueIds(value);
    }*/

    public void setContent(Component c) {
        selectedIndex = getTabbox().getSelectedIndex();
        getChildren().clear();
        appendChild(c);
    }

    public <T> T getValue() {
        return (T) crudController.getFullData();
    }

    public void setValue(List<?> value) {
        crudController.setCrudViewValue(value);
    }

    public void setValue(ArrayList<?> value) {
        this.setValue((List) value);
    }


    public void setDisabled(boolean disable) {
        EventQueues.lookup("disable-crud-" + getTypeClass().getSimpleName(), EventQueues.SESSION, true).publish(new Event("disable-crud-" + getTypeClass().getSimpleName() + "-" + getCrudViewUUID(), this, this));
    }

    public String getKlass() {
        return klass.getName();
    }
/*
    public void setKlass(String klass) throws ClassNotFoundException {
        String[] split = klass.split(":");
        if (split.length > 1) {
            String s = split[0];
            Class<?> aClass = Class.forName(s);
            this.klass = aClass;
            init(aClass);
        } else {
            if (split.length > 3) {
                String s = split[0];
                Class<?> aClass = Class.forName(s);
                this.klass = aClass;
                crudviewmode = CrudMode.MAINCRUD;
                view(aClass, getBean(PropertiesUtil.class).getFieldTable(aClass));
            }
        }
    }*/

    public void addValue(Object value) {
        crudController.addValue(value);
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


    public void addParams(String key, Object value) {
        crudController.put(key, value);
    }

    public void getParams(String key) {
        crudController.get(key);
    }

    void clearParams() {
        crudController.clearParams();
    }

    public String getBeginString() {
        return crudController.getBeginString();
    }

    public ParamMode getParamMode() {
        return crudController.getParamMode();
    }

    public void setBeginString(String beginString) {
        crudController.setBeginString(beginString);
    }

    public void setParamMode(ParamMode paramMode) {
        crudController.setParamMode(paramMode);
    }

    public void setCrudViewValue(List<?> value) {
        crudController.setCrudViewValue(value);
    }

    public List<String> getValueIds() {
        return crudController.getValueIds();
    }

    public void setValueIds(List<String> value) {
        crudController.setValueIds(value);
    }

    public Object getRoot(Object key) {
        return crudController.getRoot(key);
    }

    public Object putRoot(String key, Object value) {
        return crudController.putRoot(key, value);
    }

    public Object get(Object key) {
        return crudController.get(key);
    }

    public Object put(String key, Object value) {
        return crudController.put(key, value);
    }

    public void doQuery() {
        crudController.doQuery();
    }

    public void addEventOnEvent(CrudEvents e, OnEvent o) {
        crudController.addEventOnEvent(e, o);
    }

    public void addEventPostQuery(OnQuery o) {
        crudController.addEventPostQuery(o);
    }

    public void setContainsString(String beginString) {
        crudController.setContainsString(beginString);
    }

    public void doQueryStringBegin(String field, String value) {
        crudController.doQueryStringBegin(field, value);
    }

    public void setPage(int offSet) {
        crudController.setPage(offSet);
    }

    public void setPage(int offSet, int limit) {
        crudController.setPage(offSet, limit);
    }

    public long getCount() {
        return crudController.getCount();
    }

    public List getValues() {
        return crudController.getValues();
    }

    public int getOffSet() {
        return crudController.getOffSet();
    }

    public int getLimit() {
        return crudController.getLimit();
    }

    public Object getFullData() {
        return crudController.getFullData();
    }

    public String getOrderField() {
        return crudController.getOrderField();
    }

    public void setOrderField(String orderField) {
        crudController.setOrderField(orderField);
    }

    public OrderBY getOrderBY() {
        return crudController.getOrderBY();
    }

    public void setOrderBY(OrderBY orderBY) {
        crudController.setOrderBY(orderBY);
    }

    public void putAllRoot(Map<? extends String, ?> m) {
        crudController.putAllRoot(m);
    }
}

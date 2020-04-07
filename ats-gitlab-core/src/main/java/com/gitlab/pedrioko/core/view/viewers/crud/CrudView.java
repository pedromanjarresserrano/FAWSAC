package com.gitlab.pedrioko.core.view.viewers.crud;

import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudMode;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.controllers.CrudController;
import com.gitlab.pedrioko.core.view.viewers.crud.grid.AlphabetFilter;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

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
    Boolean reloadable;

    private @Getter
    @Setter
    CrudController crudController;
    private @Getter
    @Setter
    CrudMode crudviewmode;
    private East east;
    private North north;
    private Borderlayout borderlayout;
    private int selectedIndex = 0;

    private int pagesize = 16;
    private HashMap<Object, Object> arg;

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
        borderlayout = new Borderlayout();
        divbar = new Div();
        arg = new HashMap<>();
        arg.put("klass-crud", klass);
        arg.put("CrudView", this);
        arg.put("CrudViewUUID", UUID.randomUUID().toString());

        Component crudviewbar = null;
        crudviewbar = loadPlantilla(crudviewbar, "~./zul/crud/crudviewbar");

        divbar.appendChild(crudviewbar);
        east = new East();
        north = new North();
        north.appendChild(divbar);
        borderlayout.appendChild(north);
        east.setTitle("Filters");
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
        crudtable = loadPlantilla(crudtable, "~./zul/crud/table/crudtable");

        center.appendChild(crudtable);

        setStyle("height:100%;");
        reloadable = crudviewmode != CrudMode.SUBCRUD;
        east.addEventListener(Events.ON_VISIBILITY_CHANGE, e -> {
            if (!east.isVisible()) {
                clearParams();
                crudController.doQuery();
            }
        });
        South south = new South();
        Component pagination = null;
        pagination = loadPlantilla(pagination, "~./zul/crud/filters/pagination");

        south.appendChild(pagination);
        borderlayout.appendChild(south);

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
        if (east.getChildren().isEmpty()) {
            Component crudfilters = Executions.createComponents("~./zul/crud/filters/crudfilters.zul", null, arg);
            east.appendChild(crudfilters);

        }
        east.setStyle("width: 350px;");
        east.setOpen(!east.isOpen());
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
        if (reloadable)
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
        divbar.setVisible(!disable);
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

}

package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.lang.annotation.Reference;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.enums.CrudMode;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.core.zk.component.chosenbox.ChosenBox;
import com.gitlab.pedrioko.core.zk.component.crud.Crudbox;
import com.gitlab.pedrioko.core.zk.component.stringlistbox.StringListBox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@FieldForm
public class ListField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{List.class, Set.class};
    }

    @Override
    public Component getComponent(Field e) {
        Class<?> klass = (Class<?>) ((ParameterizedType) e.getGenericType()).getActualTypeArguments()[0];
        if (e.isAnnotationPresent(Reference.class)) {
            Class<?> value = e.getAnnotation(Reference.class).value();
            return getComponent(e, /*ef, */value);
        } else {
            if (klass.isEnum()) {
                ChosenBox chosenbox = new ChosenBox();
                chosenbox.setModel(Arrays.asList(klass.getEnumConstants()));
                return chosenbox;
            } else {
                if (klass == String.class) {
                    return new StringListBox(ReflectionZKUtil.getLabel(e.getName()));
                } else return getComponent(e, /*ef,*/ klass);
            }
        }

    }

    public Component getComponent(Field e, Class<?> klass) {
        //ef.getTabs().appendChild(new Tab((ReflectionZKUtil.getLabel(e))));
        CrudView crudView = new CrudView(klass, CrudMode.SUBCRUD);
        PropertiesUtil propertiesUtil = ApplicationContextUtils.getBean(PropertiesUtil.class);
        boolean enableSubCrudsClass = propertiesUtil
                .getEnableSubCrudsClassProperty(klass, e.getName(), true);
        //   crudView.enableCommonCrudActions(enableSubCrudsClass);
        crudView.setStyle("height:100%;");
        crudView.setReloadable(false);
        //    ef.getTabpanels().appendChild(crudView);
        //     ef.putBinding(e, crudView);
        Crudbox tabbox = new Crudbox();
        Tab tab = new Tab(e.getName());
        Tabs tabs = new Tabs();
        tab.setParent(tabs);
        tabs.setParent(tabbox);
        tabbox.getTabs().appendChild(tab);
        Tabpanels tabpanels = new Tabpanels();
        tabpanels.setParent(tabbox);
        crudView.setParent(tabpanels);
        tabbox.getTabpanels().appendChild(crudView);
        tabbox.setStyle("height:95vh;display:block;");
        crudView.setStyle("height:95vh; display:block;");
        tabpanels.setStyle("height:95vh;display:block;");
        tabbox.setSelectedTab(tab);
        return tabbox;
    }
}

package com.gitlab.pedrioko.core.view.forms;

import com.gitlab.pedrioko.core.lang.annotation.Ckeditor;
import com.gitlab.pedrioko.core.lang.annotation.ColorChooser;
import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.api.RowCustomizer;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.zk.component.ColorChooserBox;
import lombok.Getter;
import lombok.Setter;
import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.*;

import javax.persistence.Id;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityForm extends Form {

    private static final long serialVersionUID = 1L;

    private static final String CLASSES = "col-xs-10 col-sm-10 col-md-10 col-lg-10";
    private final Tabbox tabbox = new Tabbox();
    private final List<String> fields;
    private @Getter
    @Setter
    Tabpanels tabpanels = new Tabpanels();
    private @Getter
    @Setter
    Tabs tabs = new Tabs();
    private boolean window;
    private String formSclass = "container form-horizontal";


    public EntityForm(Class<?> klass) {
        super();
        fields = ApplicationContextUtils.getBean(PropertiesUtil.class).getFieldForm(klass);
        fieldToUi(klass, false);
    }


    public EntityForm(Class<?> klass, boolean window) {
        super();
        fields = ApplicationContextUtils.getBean(PropertiesUtil.class).getFieldForm(klass);
        fieldToUi(klass, window);
    }


    public EntityForm(Class<?> klass, List<String> fields) {
        this.fields = fields;
        window = false;
        fieldToUi(klass, window);
    }

    public EntityForm(Class<?> klass, List<String> fields, boolean window) {
        this.fields = fields;
        this.window = window;
        fieldToUi(klass, this.window);
    }


    private void fieldToUi(Class<?> klass, boolean window) {
        setKlass(klass);
        tabs.appendChild(new Tab(ReflectionZKUtil.getLabel(klass.getSimpleName())));
        setNewInstance(ReflectionJavaUtil.getNewInstace(klass));
        List<Field> listfield = new ArrayList<>();
        if (fields != null && !fields.isEmpty()) {
            List<Field> finalListfield = listfield;
            fields.forEach(e -> {
                finalListfield.add(ReflectionJavaUtil.getField(getKlass(), e));
            });
        } else {
            listfield = ReflectionJavaUtil.getFields(getKlass()).stream()
                    .filter(e -> !e.isAnnotationPresent(Version.class) && !e.getName().equalsIgnoreCase("serialVersionUID")
                            && !e.isAnnotationPresent(Id.class)
                            && (fields != null && !fields.isEmpty() ? fields.contains(e.getName()) : true))
                    .collect(Collectors.toList());
        }
        Div div = new Div();
        setStyle("overflow-y:auto !important;height: 99%; width: 100%;");
        div.getChildren().clear();
        div.setClass("entity-form " + CLASSES);
        Tabpanel tabpanel = new Tabpanel();
        tabpanel.setHflex("1");
        tabpanel.setStyle("overflow-y:auto !important;");
        tabpanels.appendChild(tabpanel);
        tabpanels.setHeight("95%");
        tabpanel.setHeight("100%");
        listfield.forEach(e -> div.appendChild(fieldToUiField(e)));
        div.appendChild(getActions());
        tabbox.appendChild(tabs);
        tabbox.appendChild(tabpanels);
        tabbox.setStyle("height: 100%;");
        if (getEstado() == FormStates.CREATE)
            setValueForm(getNewInstance());
        if (!window) {
            div.setClass("container form-horizontal");
            tabpanel.appendChild(div);
            appendChild(tabbox);
        } else {
            div.setClass(formSclass);
            appendChild(div);
        }

    }

    private Component fieldToUiField(Field e) {
        Div renglon = new Div();
        renglon.setId("Reglon" + e.getType().toGenericString());
        Div labeldiv = new Div();
        Label label = new Label(ReflectionZKUtil.getLabel(e));

        Div campo = new Div();
        Class<?> type = e.getType();
        if (e.isAnnotationPresent(Ckeditor.class)) {
            CKeditor c = new CKeditor();
            putBinding(e, c);
            campo.appendChild(c);
            loadReglon(renglon, labeldiv, label, campo);
            renglon.setZclass(CLASSES);
            labeldiv.setZclass("col-sm-2");
            campo.setZclass(CLASSES);
            return renglon;
        }
        if (e.isAnnotationPresent(ColorChooser.class)) {
            ColorChooserBox c = new ColorChooserBox();
            putBinding(e, c);
            campo.appendChild(c);
            loadReglon(renglon, labeldiv, label, campo);
            return renglon;
        }
        ApplicationContextUtils.getBeansOfType(FieldComponent.class).stream().filter(v -> v.getToClass() == null
                || v.getToClass().length == 0 || Arrays.asList(v.getToClass()).contains(type)).forEach(w -> {
            Component component = w.getComponent(e, this);
            if (component != null) {
                putBinding(e, component);
                ReflectionZKUtil.widthComponent(component);
                campo.appendChild(component);
                loadReglon(renglon, labeldiv, label, campo);
            }
        });

        if (e.isAnnotationPresent(NoEmpty.class)) {
            Label requerido = new Label(" (" + ReflectionZKUtil.getLabel("requerido") + ")");
            requerido.setClass("text-danger");
            labeldiv.appendChild(requerido);
        }
        ApplicationContextUtils.getBeansOfType(RowCustomizer.class).stream().filter(f -> f.getToClass().contains(type))
                .forEach(c -> c.customizer(renglon, e));
        return renglon;
    }

    public void reload() {
        getChildren().clear();
        Object value = getValue();
        fieldToUi(getKlass(), window);
        setValueForm(value);
    }

    public void oneColumnMode() {
        setReglonClass("control-group col-sm-12 col-md-12 col-lg-12");
        formSclass = "";
        reload();
    }
}

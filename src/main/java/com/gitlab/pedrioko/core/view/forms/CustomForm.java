package com.gitlab.pedrioko.core.view.forms;

import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomForm extends Form {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityForm.class);

    private transient Map<String, Class<?>> formfields;
    private Div form = new Div();
    private Map<String, Component> binding;

    private transient Map<String,Div> formRenglones = new HashMap<>();

    public CustomForm(Class<?> klass, Map<String, Class<?>> formfields) {
        this.binding = new LinkedHashMap<>();
        this.formfields = formfields;
        setKlass(klass);
        this.setNewInstance(ReflectionJavaUtil.getNewInstace(this.getKlass()));
        this.formfields.forEach(this::addField);
        this.appendChild(form);
        this.appendChild(getActions());
    }

    /**
     * @param formfields the formfields to set
     */
    public void setFormfields(Map<String, Class<?>> formfields) {
        this.formfields = formfields;
    }

    public void addField(String k, Class<?> v) {
        Component fieldcomponent;
        try {
            fieldcomponent = (Component) ReflectionJavaUtil.getNewInstace(v);
            addField(k, fieldcomponent);
        } catch (IllegalArgumentException | SecurityException e) {
            LOGGER.error("ERROR on addField()", e);

        }
    }

    public void addField(String k, Component v) {
        Div renglon = new Div();
        Div labeldiv = new Div();
        Label label = new Label(ReflectionZKUtil.getLabel(k));

        Div campo = new Div();
        try {
            if (v.getClass() == Gmaps.class) {
                Gmaps gmaps = (Gmaps) v;
                gmaps.setStyle("width:300px;height:300px;");
            }
            campo.appendChild(v);
            if (formfields.isEmpty())
                binding.put(k, v);
            else
                putBinding(this.getKlass().getDeclaredField(k), v);
            loadReglon(renglon, labeldiv, label, campo);
            formRenglones.put(k,renglon);
        } catch (IllegalArgumentException | SecurityException | NoSuchFieldException e) {
            LOGGER.error("ERROR on addField()", e);

        }
    }
    public Div getRenglon(String field){
        return this.formRenglones.get(field);
    }

    public void addEventToField(String fieldname, String event, EventListener eventListener) {
        getComponentField(fieldname).addEventListener(event, eventListener);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.core.view.forms.Form#loadReglon(org.zkoss.zul.
     * Div, org.zkoss.zul.Div, org.zkoss.zul.Label, org.zkoss.zul.Div)
     */
    @Override
    public void loadReglon(Div renglon, Div labeldiv, Label label, Div campo) {
        super.loadReglon(renglon, labeldiv, label, campo);
        form.appendChild(renglon);
    }

    public Component getComponentOfField(String field) {
        return getBinding().get(ReflectionJavaUtil.getField(getKlass(), field));
    }

    public Component getComponentField(String label) {
        return binding.get(label);
    }

}

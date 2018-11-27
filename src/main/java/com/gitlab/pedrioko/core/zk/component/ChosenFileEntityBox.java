package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.api.ChosenItem;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.StorageService;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.*;
import java.util.stream.Collectors;

public class ChosenFileEntityBox extends Bandbox {

    private static final long serialVersionUID = 1L;
    private transient Set<?> valueSelection;
    private transient Set<?> model;
    private transient Set<?> auxmodel;
    private Listbox list;
    private List<Listitem> listsitems;
    private boolean checkmark = true;

    public ChosenFileEntityBox() {
        valueSelection = new LinkedHashSet<>();
        model = new LinkedHashSet<>();
        auxmodel = new LinkedHashSet<>();
        listsitems = new ArrayList<>();
        setInstant(true);
        this.setAutodrop(true);
        this.addEventListener(Events.ON_CHANGING, e -> {

            String value = getValue();
            if (value == null || value.isEmpty()) {
                model = new LinkedHashSet<>(auxmodel);
                load();
            } else {
                model = auxmodel.stream().filter(w -> ((ChosenItem) w).getVisualName().toLowerCase().contains(value.toLowerCase())).collect(Collectors.toSet());
                LinkedList<?> list = new LinkedList<>(model);
                Collections.sort(list, (x, y) -> ((ChosenItem) x).getVisualName().toLowerCase().compareToIgnoreCase(((ChosenItem) y).getVisualName().toLowerCase()));
                model = new LinkedHashSet<>(list);
                load();
            }

            this.open();
        });
        load();
    }

    private void load() {
        getChildren().clear();
        Bandpopup popup = new Bandpopup();
        list = new Listbox();
        list.setCheckmark(checkmark);
        list.setMultiple(true);
        listsitems.clear();
        list.getChildren().clear();
        model.forEach(e -> {
            Listitem listitem = new Listitem();
            listitem.setValue(e);
            listitem.setClass("chosenbox-file-item");
            Hlayout hl = new Hlayout();
            List<FileEntity> filesEntities = ((ChosenItem) e).getFilesEntities();
            Image image = new Image(filesEntities != null && !filesEntities.isEmpty() ? ApplicationContextUtils.getBean(StorageService.class).getUrlFile(filesEntities.get(0).getFilename()) : "");
            image.setWidth("40px");
            image.setHeight("40px");
            hl.appendChild(image);
            String visualName = ((ChosenItem) e).getVisualName();
            hl.appendChild(new Label(visualName));
            Listcell cell = new Listcell();
            cell.appendChild(hl);
            listitem.appendChild(cell);
            listsitems.add(listitem);
            list.getChildren().add(listitem);
            listitem.addEventListener(Events.ON_CLICK, w -> {
                List<Listitem> listselected = new ArrayList<>(list.getSelectedItems());
                valueSelection.clear();
                listselected.forEach(x -> valueSelection.add(x.getValue()));
                setLabel();
            });
        });
        popup.getChildren().add(list);
        getChildren().add(popup);
    }


    private void setLabel() {
        List<String> collect = list.getSelectedItems().stream().map(Listitem::getValue).map(Object::toString).collect(Collectors.toList());
        String join = String.join(", ", collect);
        setValue(join);
    }

    /**
     * @return the value
     */
    public List<?> getValueSelection() {
        valueSelection.clear();
        new ArrayList<>(list.getSelectedItems()).forEach(e -> valueSelection.add(e.getValue()));
        if (valueSelection.isEmpty())
            return null;
        return new ArrayList<>(valueSelection);
    }

    /**
     * @param valueSelection
     *            the valueSelection to set
     */
    /**
     * @param valueSelection
     */
    public void setValueSelection(List<?> valueSelection) {
        this.valueSelection = new HashSet<>(valueSelection);
        Set<Listitem> set = new HashSet<>();
        valueSelection.forEach(e -> listsitems.stream()
                .filter(w -> w.getValue().toString().trim().equalsIgnoreCase(e.toString().trim()))
                .forEach(set::add));
        list.setSelectedItems(set);
        setLabel();
    }

    /**
     * @param value the value to set
     */
    public void setValue(List<?> value) {

        valueSelection = new HashSet<>(value);
        load();

    }

    public boolean isCheckmark() {
        return checkmark;
    }

    public void setCheckmark(boolean checkmark) {
        this.checkmark = checkmark;
        load();
    }

    /**
     * @param model the model to set
     */
    public void setModel(List<?> model) {
        if (model != null && !model.isEmpty()) {
            Class<?> aClass = model.get(0).getClass();
            if (ChosenItem.class.isAssignableFrom(aClass)) {
                this.model = new LinkedHashSet<>(model);
                this.auxmodel = new LinkedHashSet<>(model);
                load();
            } else {
                throw new IllegalArgumentException("Class " + aClass + " not implement interface CrudGridItem");
            }
        }
    }

}

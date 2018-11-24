package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.api.ChosenItem;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.StorageService;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChosenFileEntityBox extends Bandbox {

    private static final long serialVersionUID = 1L;
    private transient Set<?> valueSelection;
    private transient Set<?> model;
    private Listbox list;
    private List<Listitem> listsitems;

    public ChosenFileEntityBox() {
        valueSelection = new HashSet<>();
        model = new HashSet<>();
        listsitems = new ArrayList<>();
        load();
    }

    private void load() {
        getChildren().clear();
        Bandpopup popup = new Bandpopup();
        list = new Listbox();
        list.setCheckmark(true);
        list.setMultiple(true);
        model.forEach(e -> {
            Listitem listitem = new Listitem();
            listitem.setValue(e);
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

    /**
     * @param model the model to set
     */
    public void setModel(List<?> model) {
        if (model != null && !model.isEmpty()) {
            Class<?> aClass = model.get(0).getClass();
            if (ChosenItem.class.isAssignableFrom(aClass)) {
                this.model = new HashSet<>(model);
                load();
            } else {
                throw new IllegalArgumentException("Class " + aClass + " not implement interface CrudGridItem");
            }
        }
    }

}

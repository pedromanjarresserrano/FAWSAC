package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.api.ChosenItem;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.StorageService;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.*;
import java.util.stream.Collectors;

public class ChosenFileEntityBox extends Bandbox {

    private static final long serialVersionUID = 1L;

    private transient Set<?> valueSelection;
    private transient LinkedList<?> model;
    private transient LinkedList<?> auxmodel;
    private Listbox list;
    private List<Listitem> listsitems;
    private boolean checkmark = true;
    private final Bandpopup popup;
    private final EventListener<Event> eventEventListener = e -> {

        String valuex = getValue();
        if (valuex == null || valuex.isEmpty()) {
            model = new LinkedList<>(auxmodel);
            load();
        } else {
            String value = valuex.substring(valuex.lastIndexOf(',') + 1).trim();
            model = new LinkedList<>(auxmodel.stream().filter(w -> ((ChosenItem) w).getVisualName().toLowerCase().startsWith(value.toLowerCase())).collect(Collectors.toList()));
            LinkedList<?> list = new LinkedList<>(model);
            Collections.sort(list, (x, y) -> ((ChosenItem) x).getVisualName().toLowerCase().compareToIgnoreCase(((ChosenItem) y).getVisualName().toLowerCase()));
            model = new LinkedList<>(list);
            load();
        }
    };

    public ChosenFileEntityBox() {
        valueSelection = new LinkedHashSet<>();
        model = new LinkedList<>();
        auxmodel = new LinkedList<>();
        listsitems = new ArrayList<>();
        setInstant(true);
        setAutodrop(true);

        addEventListener(Events.ON_CHANGING, eventEventListener);
        addEventListener(Events.ON_CHANGE, eventEventListener);
        popup = new Bandpopup();
        popup.setSclass("chosen-file-entity-popup");
        getChildren().add(popup);
        load();
    }

    private void load() {
        popup.getChildren().clear();
        list = new Listbox();
        list.setCheckmark(checkmark);
        list.setMultiple(true);
        listsitems.clear();
        list.setStyle("overflow-y: auto !important;");
        list.getChildren().clear();
        Listitem linull = new Listitem();
        Listcell child = new Listcell();
        child.setLabel("Unassaignment");
        child.setHeight("50px");
        linull.appendChild(child);
        linull.setValue(null);
        setListener(linull);
        linull.setClass("chosenbox-file-item");
        listsitems.add(linull);
        list.getChildren().add(linull);
        model.forEach(e -> {
            Listitem listitem = new Listitem();
            listitem.setValue(e);
            listitem.setClass("chosenbox-file-item");
            Hlayout hl = new Hlayout();

            List<FileEntity> filesEntities = ((ChosenItem) e).getFilesEntities();
            Image image = new Image(filesEntities != null && !filesEntities.isEmpty() ? ApplicationContextUtils.getBean(StorageService.class).getUrlFile(filesEntities.get(0).getFilename()) : "");
            image.setWidth("40px");
            image.setHeight("50px");
            listitem.setStyle("width:90%");
            hl.appendChild(image);
            String visualName = ((ChosenItem) e).getVisualName();
            hl.appendChild(new Label(visualName));
            Listcell cell = new Listcell();
            cell.appendChild(hl);
            listitem.appendChild(cell);
            listsitems.add(listitem);
            list.getChildren().add(listitem);
            setListener(listitem);
            if (valueSelection.contains(e)) {
                list.setSelectedItem(listitem);
                setLabel();
            }
        });
        setStyle("overflow-x:hidden;");
        popup.getChildren().add(list);
    }

    private void setListener(Listitem listitem) {
        listitem.addEventListener(Events.ON_CLICK, evt -> {
            Listitem target = (Listitem) evt.getTarget();
            if (target.isSelected()) {
                valueSelection.add(target.getValue());
            } else {
                valueSelection.remove(target.getValue());
            }
            setLabel();
        });
    }


    private void setLabel() {
        List<String> collect = valueSelection.stream().map(e -> {
            if (e == null) {
                return "Unassaignment";
            } else return e.toString();
        }).collect(Collectors.toList());
        String join = String.join(", ", collect);
        setValue(join + (collect.isEmpty() ? "" : ", "));
    }

    /**
     * @return the value
     */
    public List<?> getValueSelection() {
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
        if (valueSelection != null) {
            this.valueSelection = new HashSet<>(valueSelection);
            Set<Listitem> set = new HashSet<>();
            valueSelection.forEach(e -> listsitems.stream()
                    .filter(w -> w.getValue() != null)
                    .filter(w -> w.getValue().toString().trim().equalsIgnoreCase(e.toString().trim()))
                    .forEach(set::add));
            list.setSelectedItems(set);
            setLabel();
        }
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
                LinkedList<?> model1 = new LinkedList<>(model);
                this.model = model1;
                auxmodel = new LinkedList<>(model);
                load();
            } else {
                throw new IllegalArgumentException("Class " + aClass + " not implement interface CrudGridItem");
            }
        }
    }

}

package com.gitlab.pedrioko.core.zk.component.chosenbox;

import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChosenBox extends Bandbox {

    private static final long serialVersionUID = 1L;
    private transient Set<?> valueSelection;
    private transient Set<?> model;
    private Listbox list;
    private List<Listitem> listsitems;

    public ChosenBox() {
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
            Listitem listitem = new Listitem(ReflectionZKUtil.getLabel(e.toString()), e);
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
        List<String> collect = list.getSelectedItems().stream().map(Listitem::getLabel).collect(Collectors.toList());
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
        this.model = new HashSet<>(model);
        load();
    }

}
